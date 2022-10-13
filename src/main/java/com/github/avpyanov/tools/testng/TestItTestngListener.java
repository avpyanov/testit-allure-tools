package com.github.avpyanov.tools.testng;

import com.github.avpyanov.tools.annotations.AutotestId;
import com.github.avpyanov.tools.Settings;
import org.aeonbits.owner.ConfigFactory;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.List;

import static com.github.avpyanov.tools.allure.TestRunUtils.getAutotestIdsFromTestRun;

public class TestItTestngListener implements IMethodInterceptor {

    final Settings settings = ConfigFactory.create(Settings.class);

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {
        final List<IMethodInstance> result = new ArrayList<>();
        final List<String> testIdList = getAutotestIdsFromTestRun(settings.testRunId());
        if (!testIdList.isEmpty()){
            for (IMethodInstance iMethodInstance : list) {
                String testId = getTestId(iMethodInstance);
                if (testIdList.contains(testId)) {
                    result.add(iMethodInstance);
                }
            }
        }
        return result;
    }

    private String getTestId(IMethodInstance instance) {
        AutotestId annotation = instance.getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(AutotestId.class);
        if (annotation != null) {
            return annotation.value();
        } else return "";
    }
}