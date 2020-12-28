package pl.jakubtrzcinski.priap;

import lombok.experimental.UtilityClass;
import pl.jakubtrzcinski.priap.api.EventListener;

import java.lang.reflect.ParameterizedType;


@UtilityClass
public class GenericsUtils {
    public Class<?> getGeneric(EventListener obj){
        return (Class<?>) ((ParameterizedType) obj.getClass()
                .getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }
}
