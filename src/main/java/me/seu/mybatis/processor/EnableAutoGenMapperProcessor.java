package me.seu.mybatis.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import me.seu.mybatis.annotation.EnableAutoGenMapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@AutoService(Processor.class)
public class EnableAutoGenMapperProcessor extends AbstractProcessor {
    private Filer filer;
    /**
     * 元素帮助类
     */
    public Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(EnableAutoGenMapper.class.getCanonicalName());
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
        for (Element rootElement : roundEnv.getElementsAnnotatedWith(EnableAutoGenMapper.class)) {
            EnableAutoGenMapper genMapper = rootElement.getAnnotation(EnableAutoGenMapper.class);
            ClassName baseMapper = ClassName.get(getMapper(genMapper.superMapperClassName(), true), getMapper(genMapper.superMapperClassName(), false));
            Set<String> packageNames = new LinkedHashSet<>();
            for (String modelPackageName : genMapper.modelPackageName()) {
                packageNames.add(modelPackageName);
                packageNames.addAll(getPackageNames(modelPackageName));
            }
            Set<TypeElement> typeElements = new LinkedHashSet<>();
            for (String modelPackageName : packageNames) {
                PackageElement packageElement = elementUtils.getPackageElement(modelPackageName);
                for (Element findElement : packageElement.getEnclosedElements()) {
                    TypeElement typeElement = (TypeElement) findElement;
                    if(typeElement.getAnnotation(Table.class)== null){
                        continue;
                    }
                    typeElements.add(typeElement);
                }
            }
            for (TypeElement typeElement : typeElements) {
                try {
                    String shortClassName = getShortClassName(typeElement);
                    String mapperName = genMapper.mapperPrefix() + shortClassName + "Mapper";
                    ClassName basemapper = ClassName.get(getMapper(genMapper.superMapperClassName(), true), getMapper(genMapper.superMapperClassName(), false));
                    ClassName model = ClassName.get(typeElement);
                    TypeName superinterface = ParameterizedTypeName.get(basemapper, model);
                    TypeSpec mapper = TypeSpec.interfaceBuilder(mapperName)
                            .addModifiers(Modifier.PUBLIC)
                            .addSuperinterface(superinterface)
                            .build();
                    JavaFile javaFile = JavaFile.builder(genMapper.mapperPackageName(), mapper)
                            .addFileComment(" This codes are generated automatically. Do not modify!")
                            .build();
                    javaFile.writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public String getMapper(String fullClass, boolean packageflag) {
        int index = fullClass.lastIndexOf(".");
        if (packageflag) {
            return fullClass.substring(0, index);
        }
        return fullClass.substring(index + 1);
    }

    private Set<String> getPackageNames(String rootPackageName) {
        Set<String> packageNames = new LinkedHashSet<>();
        StringBuilder dirUrl = new StringBuilder(System.getProperty("user.dir"));
        dirUrl.append(File.separator).append("src");
        dirUrl.append(File.separator).append("main");
        dirUrl.append(File.separator).append("java");
        dirUrl.append(File.separator).append(rootPackageName.replace(".", File.separator));
        String rootPath = dirUrl.toString();
        File rootFile = new File(rootPath);
        if (rootFile.exists() && rootFile.isDirectory()) {
            for (File file : rootFile.listFiles()) {
                if (file.isDirectory()) {
                    String dirname = file.getAbsolutePath().replace(rootPath, "").replace(File.separator, ".");
                    String packageName = rootPackageName + dirname;
                    packageNames.add(packageName);
                    packageNames.addAll(getPackageNames(packageName));
                }
            }
        }
        return packageNames;
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
