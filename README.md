# Batch Migration

[![Build project](https://github.com/Romanow/batch-migration/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/Romanow/batch-migration/actions/workflows/build.yml)

### Build

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