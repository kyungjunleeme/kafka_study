
https://knative.dev/docs/eventing/brokers/broker-types/kafka-broker/#installation
```shell
ubuntu@nks-bastion:~/workspace/data-pipeline$ k get pods -A -o wide | grep kafka
knative-eventing    kafka-controller-XXXXX                                 1/1     Running     0             59d    198.XXX.XXX.XXX    ai-XXXX   <none>           <none>
knative-eventing    kafka-webhook-eventing-XXXX                          1/1     Running     0             59d    198.XXX.XXX.XXX     ai-XXXX   <none>           <none>
```