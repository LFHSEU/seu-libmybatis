package me.seu.mybatis;

import lombok.extern.slf4j.Slf4j;
import me.seu.mybatis.exception.GenericMapperException;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AssignableTypeFilter;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by liangfeihu on 2016/11/14.
 */
@Slf4j
public class MapperClassPathScanningProvider extends ClassPathScanningCandidateComponentProvider {
    public MapperClassPathScanningProvider() {
        super(true);
    }

    private Set<String> interfaceNames = new HashSet<>();

    public Map<String, Class> findMapper(String basePackage, String interfaceName) {
        Map<String, Class> mapperMap = new HashMap<>();
        try {
            if (!interfaceNames.contains(interfaceName)) {
                super.addIncludeFilter(new AssignableTypeFilter(Class.forName(interfaceName)) {
                    @Override
                    protected boolean matchClassName(String className) {
                        return false;
                    }
                });
            }
            String ex = "classpath*:" + this.resolveBasePackage(basePackage) + "/**/*.class";
            Resource[] resources = ((ResourcePatternResolver) super.getResourceLoader()).getResources(ex);
            for (Resource resource : resources) {
                MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
                if (super.isCandidateComponent(metadataReader)) {
                    Type type = ((ParameterizedTypeImpl) Class.forName(metadataReader.getClassMetadata().getClassName()).getGenericInterfaces()[0]).getActualTypeArguments()[0];
                    log.warn("find model class = {} , mapper class = {}",type.getTypeName(),metadataReader.getClassMetadata().getClassName());
                    mapperMap.put(type.getTypeName(), Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        }catch (Exception e){
            throw new GenericMapperException("mapper definition exception");
        }

        return mapperMap;
    }
}
