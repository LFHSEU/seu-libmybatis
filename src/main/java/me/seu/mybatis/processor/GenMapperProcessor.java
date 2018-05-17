package me.seu.mybatis.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import me.seu.mybatis.annotation.GenMapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@AutoService(Processor.class)
public class GenMapperProcessor extends AbstractProcessor {
    private Filer filer;
    /**
     * 元素帮助类
     */
    public Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(GenMapper.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(GenMapper.class)) {
            TypeElement typeElement = (TypeElement) element;
            GenMapper genMapper = element.getAnnotation(GenMapper.class);
            String packageName = genMapper.packageName().length() != 0 ? genMapper.packageName() : getPackageName(element) + ".mapper";
            String shortClassName = getShortClassName(element);
            String mapperName = genMapper.mapperName().length() == 0 ? shortClassName + "Mapper" : genMapper.mapperName();

            try {
                ClassName basemapper = ClassName.get(getMapper(genMapper.superMapperClassName(),true), getMapper(genMapper.superMapperClassName(),false));
                ClassName model = ClassName.get(typeElement);
                TypeName superinterface = ParameterizedTypeName.get(basemapper, model);
                TypeSpec mapper = TypeSpec.interfaceBuilder(mapperName)
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(superinterface)
                        .build();
                JavaFile javaFile = JavaFile.builder(packageName, mapper)
                        .addFileComment(" This codes are generated automatically. Do not modify!")
                        .build();
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public  String getMapper(String fullClass, boolean packageflag) {
        int index = fullClass.lastIndexOf(".");
        if (packageflag) {
            return fullClass.substring(0, index);
        }
        return fullClass.substring(index + 1);
    }


    private String getPackageName(Element element) {
        String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        return packageName.substring(0, packageName.lastIndexOf("."));
    }

    private String getShortClassName(Element element) {
        String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        int packageLen = packageName.length() + 1;
        return getFullClassName(element).substring(packageLen).replace('.', '$');
    }

    public String getFullClassName(Element element) {
        return ((TypeElement) element).getQualifiedName().toString();
    }
}
