package com.itihub.esjob.task;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        MyDataflowJob a = new MyDataflowJob();

        System.out.println(DataflowJob.class.isInstance(a));
        Class<?>[] interfaces = a.getClass().getInterfaces();
        System.out.println(Arrays.toString(interfaces));
        for (Class<?> anInterface : interfaces) {
            if (ElasticJob.class.isAssignableFrom(anInterface)){
//            if (anInterface instanceof ElasticJob){
//            if (anInterface.isAssignableFrom(ElasticJob.class)){
                System.out.println("是ElasticJob的子类");
            }
        }
    }
}
