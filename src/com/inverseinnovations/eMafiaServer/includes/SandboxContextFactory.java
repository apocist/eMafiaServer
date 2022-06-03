package com.inverseinnovations.eMafiaServer.includes;

import org.mozilla.javascript.*;

public class SandboxContextFactory extends ContextFactory {
	@Override
	protected Context makeContext() {
		Context cx = super.makeContext();
		cx.setWrapFactory(new SandboxWrapFactory());
		return cx;
	}

}

class SandboxNativeJavaObject extends NativeJavaObject {
	private static final long serialVersionUID = 1L;
	public SandboxNativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType) {
		super(scope, javaObject, staticType);
	}

	@Override
	public Object get(String name, Scriptable start) {
		if (name.equals("getClass")) {
			return NOT_FOUND;
		}

		return super.get(name, start);
	}
}

class SandboxWrapFactory extends WrapFactory {
	@Override
	public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, @SuppressWarnings("rawtypes") Class staticType) {
		return new SandboxNativeJavaObject(scope, javaObject, staticType);
	}
}
