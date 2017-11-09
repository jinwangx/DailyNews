package Lib.callback;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 创建时间：
 * 更新时间 2017/10/30 15:07
 * 版本：
 * 作者：Mr.jin
 * 描述：网络调用回调，支持泛型
 */

public abstract class ObjectCallBack<T> {
	private Class<List<T>> clazz;

	@SuppressWarnings("unchecked")
	public ObjectCallBack() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		this.clazz = (Class<List<T>>) type.getActualTypeArguments()[0];
	}

	public abstract void onSuccess(List<T> t);

	public abstract void onError(int error, String msg);

	public Class<List<T>> getClazz() {
		return clazz;
	}

}
