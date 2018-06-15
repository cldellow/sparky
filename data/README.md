# data

Scripts in here get executed on target machines by... magic? and put their output... somewhere?
and... somehow? that manifests in the data being available to the web server.

## Format

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
