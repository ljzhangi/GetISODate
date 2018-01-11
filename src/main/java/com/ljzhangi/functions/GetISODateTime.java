package com.ljzhangi.functions;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.text.SimpleDateFormat;
import java.util.*;

public class GetISODateTime extends AbstractFunction {

    // 自定义Function的描述
    private static final List<String> desc = new LinkedList<String>();
    private static final Map<String, Integer> mapping = new HashMap<String, Integer>();
    static {
        desc.add("距离当前时间的数字");
        desc.add("单位（years/months/days(default)/hours/minutes/seconds）");
        desc.add("日期格式（option）");
    }

    // function的名称
    private static final String KEY = "__getISODatetime";

    // 传入参数的值
    private Object[] values;
    private String delta, field, format;

    public List<String> getArgumentDesc() {
        return desc;
    }

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String datetime;
        String formatter;
        if (field.length() == 0){
            field = "days";
        }

        if (format.length() == 0){
            formatter = "yyyy-MM-dd'T'HH:mm:ss.S";
        } else {
            formatter = format;
        }

        Calendar calendar = Calendar.getInstance();
        mapping.put("years", Calendar.YEAR);
        mapping.put("months", Calendar.MONTH);
        mapping.put("days", Calendar.DATE);
        mapping.put("hours", Calendar.HOUR);
        mapping.put("minutes", Calendar.MINUTE);
        mapping.put("seconds", Calendar.SECOND);
        calendar.add(mapping.get(field), (new Integer(delta)));
        Date date = calendar.getTime();

        datetime = new SimpleDateFormat(formatter).format(date).concat("Z");
        return datetime;
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters, 1, 3);
        values = parameters.toArray();

        delta = ((CompoundVariable) values[0]).execute().trim();

        if (values.length > 1) {
            field = ((CompoundVariable) values[1]).execute().trim();
        }

        if (values.length > 2) {
            format = ((CompoundVariable) values[2]).execute().trim();
        }
    }
}
