package org.athrun.android.framework.transform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.athrun.android.framework.transform.action.ActionType;
import org.athrun.android.framework.transform.action.BaseAction;
import org.athrun.android.framework.transform.action.DeviceAction;
import org.athrun.android.framework.transform.action.IAction;
import org.athrun.android.framework.transform.action.OptionItemAction;
import org.athrun.android.framework.transform.action.ViewAction;
import org.athrun.android.framework.transform.taobaoview.TaobaoSkuViewAction;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class AthrunTestCase {
	
	private static final String NEWLINE = "blank();";
	private static final String COMMENT = "comment";

	private File xmlFile;

	private List<IAction> actions = new ArrayList<IAction>();

	private AST ast;
	private CompilationUnit compilationUnit;
	private TypeDeclaration athrunTestCaseClass;

	private ActionFactory actionFactory;

	private String testCaseName;
	private String testMethodName;
	private String mainActivityName;
	
	private static ArrayList<String> imports = new ArrayList<String>();
	
	static {
		imports.add("org.athrun.android.framework.AthrunTestCase");
		imports.add("org.athrun.android.framework.viewelement.VieweElement");
		imports.add("org.athrun.android.framework.viewelement.AbsListViewElement");
		imports.add("org.athrun.android.framework.viewelement.CheckableElement");
		imports.add("org.athrun.android.framework.viewelement.ScrollViewElement");
		imports.add("org.athrun.android.framework.viewelement.SlideableElement");
		imports.add("org.athrun.android.framework.viewelement.TextViewElement");
		imports.add("org.athrun.android.framework.viewelement.ToastElement");
		imports.add("org.athrun.android.framework.viewelement.ViewGroupElement");
		imports.add("org.athrun.android.framework.viewelement.VieweElement");
		imports.add("org.athrun.android.framework.AthrunDevice");
	}
	
	public AthrunTestCase(File xmlFile) {
		this.ast = AST.newAST(AST.JLS3);
		this.compilationUnit = ast.newCompilationUnit();
		this.xmlFile = xmlFile;
		this.actionFactory = ActionFactory.getInstance(ast);
	}

	void init(String testCaseName, String testPackageName, String mainActivity,
			String testMethodName) {
		this.testCaseName = testCaseName;
		this.testMethodName = testMethodName;
		this.mainActivityName = mainActivity;

		initActions();

		createTestCaseClass(this.testCaseName);
		setPackage(testPackageName);
		setImports(imports);
		setConstructor(getAppPackageName(), this.mainActivityName);
	}

	private String getAppPackageName() {
		return XmlParser.getRootElement(this.xmlFile).attributeValue(
				"packagename");
	}

	@SuppressWarnings("unchecked")
	private void createTestCaseClass(String testCaseName) {
		athrunTestCaseClass = ast.newTypeDeclaration();
		athrunTestCaseClass.setSuperclassType(ast.newSimpleType(ast
				.newSimpleName("AthrunTestCase")));
		athrunTestCaseClass.setName(ast.newSimpleName(testCaseName));
		athrunTestCaseClass.modifiers().add(
				ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
		compilationUnit.types().add(athrunTestCaseClass);
	}

	private void setPackage(String testPackageName) {
		PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
		packageDeclaration.setName(ast.newName(testPackageName));
		this.compilationUnit.setPackage(packageDeclaration);
	}

	@SuppressWarnings("unchecked")
	private void setImports(List<String> packages) {
		for (String imp : packages) {
			ImportDeclaration importDeclaration = ast.newImportDeclaration();
			importDeclaration.setName(ast.newName(imp));
			compilationUnit.imports().add(importDeclaration);
		}
	}

	@SuppressWarnings("unchecked")
	private void setConstructor(String appPkgName, String mainActivity) {
		this.mainActivityName = mainActivity;
		MethodDeclaration constructor = ast.newMethodDeclaration();
		constructor.setConstructor(true);
		constructor.thrownExceptions().add(ast.newSimpleName("Exception"));
		constructor.setName(ast.newSimpleName(this.testCaseName));
		constructor.modifiers().add(
				ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		Block constructBlock = ast.newBlock();
		constructor.setBody(constructBlock);
		SuperConstructorInvocation superConstructorInvocation = ast
				.newSuperConstructorInvocation();
		constructBlock.statements().add(superConstructorInvocation);

		StringLiteral appPackage = ast.newStringLiteral();
		StringLiteral appMainActivity = ast.newStringLiteral();
		appPackage.setLiteralValue(appPkgName);
		appMainActivity.setLiteralValue(this.mainActivityName);

		superConstructorInvocation.arguments().add(appPackage);
		superConstructorInvocation.arguments().add(appMainActivity);
		this.athrunTestCaseClass.bodyDeclarations().add(constructor);
	}

	@SuppressWarnings("unchecked")
	private Block initTestMethod() {

		MethodDeclaration testMethodDeclaration = ast.newMethodDeclaration();
		testMethodDeclaration.setName(ast.newSimpleName(this.testMethodName));
		testMethodDeclaration.thrownExceptions().add(
				ast.newSimpleName("Exception"));
		testMethodDeclaration.modifiers().add(
				ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
		testMethodDeclaration.setReturnType2(ast
				.newPrimitiveType(PrimitiveType.VOID));

		Block testMethodBlock = ast.newBlock();
		testMethodDeclaration.setBody(testMethodBlock);

		athrunTestCaseClass.bodyDeclarations().add(testMethodDeclaration);

		return testMethodBlock;
	}

	private void addTestStatements(Block testMethodBlock) {
		for (IAction action : actions) {
			action.toJavaCode(testMethodBlock);
		}
	}

	private void initActions() {
		List<Map<String, String>> xmlActions = XmlParser
				.getAllActions(this.xmlFile);

		for (Map<String, String> action : xmlActions) {
			IAction iAction = null;

			if (isInAbsListView(action)) {
				iAction = this.actionFactory.getAction(action,
						ActionType.AbsListViewAction);

			} else if (isInViewGroup(action)) {
				iAction = this.actionFactory.getAction(action,
						ActionType.ViewGroupAction);

			} else if (isTaobaoSkuViewAction(action)) {
				iAction = this.actionFactory.getAction(action,
						ActionType.TaobaoSkuViewAction);
			} else if (isTextView(action)) {
				iAction = this.actionFactory.getAction(action,
						ActionType.TextViewAction);

			} else if (isOptionItemAction(action)) {
				iAction = this.actionFactory.getAction(action,
						ActionType.OptionItemAction);

			} else if (isDevice(action)) {
				iAction = this.actionFactory.getAction(action,
						ActionType.DeviceAction);

			} else {
				iAction = this.actionFactory.getAction(action,
						ActionType.ViewAction);
			}

			this.actions.add(iAction);
		}
	}

	private boolean isTextView(Map<String, String> action) {
		return action.containsKey("viewtext")
				|| action.containsKey("inputtext");
	}

	private boolean isInAbsListView(Map<String, String> action) {
		return (isInViewGroup(action) && action.containsKey("parenttype") && (action
				.get("parenttype").contains("ListView") || action.get(
				"parenttype").contains("GridView")));
	}

	private boolean isInViewGroup(Map<String, String> action) {
		return action.get(BaseAction.ACTION_TYPE).equalsIgnoreCase(
				ViewAction.ITEM_CLICK) || action.get(BaseAction.ACTION_TYPE).equalsIgnoreCase(
						ViewAction.ITEM_LONG_CLICK);
	}

	private boolean isDevice(Map<String, String> action) {
		return action.get(BaseAction.ACTION_TYPE).equalsIgnoreCase(
				DeviceAction.PRESS_KEY);
	}

	private boolean isOptionItemAction(Map<String, String> action) {
		return action.get(BaseAction.ACTION_TYPE).equalsIgnoreCase(
				OptionItemAction.OPTION_ITEM);
	}

	private boolean isTaobaoSkuViewAction(Map<String, String> action) {
		return action.get(BaseAction.ACTION_TYPE).equalsIgnoreCase(
				TaobaoSkuViewAction.SKU_CLICK);
	}

	private String toJavaCode() {
		return this.compilationUnit.toString();
	}

	void toJavaFile(String path) {
		try {
			FileUtils.writeStringToFile(new File(path), format(this.toJavaCode()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String format(String javaCode) {
		String codeWithComment = formatComment(javaCode);
		return formatBlank(codeWithComment);
	}
	
	private String formatComment(String javaCode) {
		return javaCode.replace(COMMENT, "//");
	}
	
	private String formatBlank(String javaCode) {
		return javaCode.replace(NEWLINE, "\r");
	}

	public static void main(String args[]) {
		AthrunTestCase testCase = new AthrunTestCase(new File(
				"./res/UIevent11.xml"));
		testCase.init("Test", "com.taobao.android.client.test",
				"com.taobao.taobao.MainActivity2", "testBug");
		testCase.addTestStatements(testCase.initTestMethod());
		System.out.println(testCase.format(testCase.toJavaCode()));
		testCase.toJavaFile("./gen/TestLogIn.java");
	}
}
