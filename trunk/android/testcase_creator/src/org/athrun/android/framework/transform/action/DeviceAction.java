package org.athrun.android.framework.transform.action;

import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;

public class DeviceAction extends BaseAction {
	private static final String KEY_CODE = "keycode";
	
	public static final String PRESS_KEY = "keypress";
	private static final String SLIDE = "slide";
	
	private static final String GETDEVICE = "getDevice";
	private static final String PRESSKEYS = "pressKeys";
	
	private String keyCode;
	
	public DeviceAction(Map<String, String> action, AST ast) {
		super(action, ast);
		this.keyCode = action.get(KEY_CODE);
	}

	@Override
	public void toJavaCode(Block methodBlock) {
		createComment(methodBlock);
		
		if (actiontype.equalsIgnoreCase(PRESS_KEY)) {
			if (this.keyCode.contains("MENU")) {
				createSleep(methodBlock);
			}
			createPressKey(methodBlock);
			
		} else if (actiontype.equalsIgnoreCase(SLIDE)) {
			//do nothing now
		}
		
		createBlank(methodBlock);
	}
	
	@SuppressWarnings("unchecked")
	private void createPressKey(Block methodBlock) {
		MethodInvocation pressKey = ast.newMethodInvocation();
		MethodInvocation getDevice = ast.newMethodInvocation();
		getDevice.setName(ast.newSimpleName(GETDEVICE));

		pressKey.setExpression(getDevice);
		pressKey.setName(ast.newSimpleName(PRESSKEYS));
		
		pressKey.arguments().add(getKeyCode(action));
		
		methodBlock.statements().add(ast.newExpressionStatement(pressKey));
	}
	
	private Name getKeyCode(Map<String, String> action) {
		Name keyParam = ast.newName(getSimpleNames(this.keyCode));
		return keyParam;
	}
}
