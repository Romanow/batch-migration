# Batch Migration

[![Build project](https://github.com/Romanow/batch-migration/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/Romanow/batch-migration/actions/workflows/build.yml)

## Сборка и запуск

```shell
$ ./gradlew clean build
$ docker compose build
```

### Run local k8s cluster

Запускаем локальный кластер k8s в docker (папка [`k8s`](k8s)):

```shell
$ kind create cluster --config kind.yml
$ kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
```

Загружаем docker images в кластер `kind` (папка [`scripts`](scripts)):

```shell
$ ./scripts/load-images.sh
```

Загружаем репозитории:

```shell
$ helm repo add romanow https://romanow.github.io/helm-charts/
$ helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
$ helm repo update
```

Устанавливаем инструменты для мониторинга кластера:

```shell
$ helm install kube-state-metrics prometheus-community/kube-state-metrics --set image.tag=v2.6.0 

$ helm install prometheus -f prometheus/values.yaml romanow/prometheus

$ helm install grafana -f grafana/values.yaml romanow/grafana
```

Запускаем `migration-service`:

```shell
$ helm install postgres romanow/postgres --values postgres/values.yaml      

$ helm install migration-service romanow/java-service --values migration-service/values.yaml      
```

### Тестирование

![Spring Batch Architecture](images/Spring%20Batch%20Architecture.png)

При параллельном выполнении в 10 потоков с блоками по 5000 записей на 2CPU, 4Gb:

```
Step: [migrate:partition9] executed in 3m29s368ms
Step: [migrate:partition6] executed in 3m31s859ms
Step: [migrate:partition7] executed in 3m35s659ms
Step: [migrate:partition8] executed in 3m42s162ms
Step: [migrate:partition0] executed in 3m42s171ms
Step: [migrate:partition4] executed in 3m42s858ms
Step: [migrate:partition2] executed in 3m44s359ms
Step: [migrate:partition3] executed in 3m44s359ms
Step: [migrate:partition5] executed in 3m44s587ms
Step: [migrate:partition1] executed in 3m45s27ms
Step: [migrate:manager] executed in 3m45s258ms
Executing step: [delete]
Removed 400000 items from staged
Step: [delete] executed in 654ms

Job: [SimpleJob: [name=migration]]
completed with the following parameters:
[
  {
    "solveId": {
      "value": "solve_id",
      "type": "class java.lang.String",
      "identifying": true
    },
    "key": {
      "value": "2023-03-15T09:23:24.955656258",
      "type": "class java.lang.String",
      "identifying": true
    }
  }
]
and the following status: [COMPLETED] in 4m6s649ms
```