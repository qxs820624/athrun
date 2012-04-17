package org.athrun.android.framework.transform.action;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.ThisExpression;

public abstract class BaseAction implements IAction {
	private static final String COMMENT = "comment";
	private static final String BLANK = "blank";
	private static final String SLEEP = "sleep";

	public static final String ACTION_TYPE = "actiontype";

	protected String actiontype;
	protected String activity;

	protected AST ast;
	protected Map<String, String> action;

	public BaseAction(Map<String, String> action, AST ast) {
		this.action = action;
		this.actiontype = action.get(ACTION_TYPE);
		this.ast = ast;
	}

	public abstract void toJavaCode(Block methodBlock);

	@SuppressWarnings("unchecked")
	protected void createComment(Block methodBlock) {
		MethodInvocation methodInvocation = this.ast.newMethodInvocation();
		methodInvocation.setName(ast.newSimpleName(COMMENT));
		StringLiteral content = ast.newStringLiteral();
		content.setLiteralValue(this.action.toString());
		methodInvocation.arguments().add(content);

		methodBlock.statements().add(
				this.ast.newExpressionStatement(methodInvocation));
	}

	@SuppressWarnings("unchecked")
	protected void createBlank(Block methodBlock) {
		MethodInvocation methodInvocation = this.ast.newMethodInvocation();
		methodInvocation.setName(ast.newSimpleName(BLANK));

		methodBlock.statements().add(
				this.ast.newExpressionStatement(methodInvocation));
	}

	@SuppressWarnings("unchecked")
	protected void createSleep(Block methodBlock) {
		MethodInvocation methodInvocation = this.ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("Thread"));
		methodInvocation.setName(ast.newSimpleName(SLEEP));
		Name time = ast.newName(getSimpleNames("IViewElement.ANR_TIME"));
		methodInvocation.arguments().add(time);
		methodBlock.statements().add(
				ast.newExpressionStatement(methodInvocation));

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String[] getSimpleNames(String qualifiedName) {
		StringTokenizer st = new StringTokenizer(qualifiedName, ".");
		ArrayList list = new ArrayList();
		while (st.hasMoreTokens()) {
			String name = st.nextToken().trim();
			if (!name.equals("*"))
				list.add(name);
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
}
