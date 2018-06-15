# data

## Driver

Pick a Spark job. Go to its Environment tab and get the `spark.app.id`.

Then run:

```
$ watch 6251715f-a1d3-4b62-9106-af1eb207f795-187895
```

A file will be put in `/tmp/sparky` with a condensed summary of events, like:

```
{"e":"SparkListenerEnvironmentUpdate","app_name":"com.snapsort.adreports.spark.RevenueImpressionByNetworkLoader$","app_id":"6251715f-a1d3-4b62-9106-af1eb207f795-187543","requested_cores":"120","role":"prod2","driver_mem":"10G","executor_mem":"12G"}
{"e":"SparkListenerExecutorAdded","ts":1529083389208,"ex_id":"25","host":"172.30.5.7"}
{"e":"SparkListenerExecutorAdded","ts":1529083389213,"ex_id":"20","host":"172.30.5.248"}
{"e":"SparkListenerExecutorAdded","ts":1529083389216,"ex_id":"6","host":"172.30.5.14"}
{"e":"SparkListenerExecutorRemoved","ts":1529068624378,"ex_id":"30","reason":"Remote RPC client disassociated. Likely due to containers exceeding thresholds, or network issues. Check driver logs for WARN messages."}
{"e":"SparkListenerJobStart","ts":1529083519127,"id":86,"desc":"writePartitionData to s3://sortable-ads-dw/tables/ssp_http/y=2018/m=6/d=15/h=17/","stages":[{"id":122,"task_count":84},{"id":123,"task_count":26}]}
{"e":"SparkListenerTaskStart","ts":1529083897064,"attempt":0,"ex_id":"1","id":3633,"stage_id":0}
{"e":"SparkListenerTaskEnd","ts":1529083900150,"reason":{"Reason":"Success"},"failed":false,"killed":false}
{"e":"SparkListenerJobEnd","ts":1529083900151,"id":85,"result":"JobSucceeded"}
```


## Workers

Scripts in here get executed on target machines by... magic? and put their output... somewhere?
and... somehow? that manifests in the data being available to the web server.

### Format

The output should look like:

```
sec-since-epoch metric:name:value
```

eg:

```
$ ssh mesos-slave-prod1 < cpu.sh
1529067242 172.30.5.220:cpu:0.397373
1529067243 172.30.5.220:cpu:0.793532
1529067244 172.30.5.220:cpu:0.758105
1529067245 172.30.5.220:cpu:0.764411
1529067246 172.30.5.220:cpu:0.771144
```

This shows CPU usage for the 172.30.5.220 host.
