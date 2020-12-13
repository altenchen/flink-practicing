package self.practice.config;

import java.io.Serializable;

/**
 * @author altenchen
 * @create 功能
 */
public class MysqlProperties implements Serializable {

    private Class mapperClass;
    private String methodName;
    private Object[] param;

    public MysqlProperties(Class mapperClass, String methodName, Object... param){
        this.mapperClass = mapperClass;
        this.methodName = methodName;
        this.param = param;
    }

    public Class getMapperClass() {
        return mapperClass;
    }

    public void setMapperClass(Class mapperClass) {
        this.mapperClass = mapperClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParam() {
        return param;
    }

    public void setParam(Object[] param) {
        this.param = param;
    }
}
