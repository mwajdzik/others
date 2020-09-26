## EFK - ElasticSearch + FluentD + Kibana
- minikube addons enable efk
- wait for Kibana (10-15 minutes)
- minikube addons open efk
- kubectl apply -f .
- kubectl get pods
- kubectl get services
- open http://192.168.99.115/products
- check Kibana

#### If issues with pulling images [link](https://github.com/kubernetes/minikube/issues/4589)

```
minikube ssh
sudo vi /etc/systemd/network/10-eth1.network add DNS=8.8.8.8 under [Network]
sudo vi /etc/systemd/network/20-dhcp.network add DNS=8.8.8.8 under [Network]
sudo systemctl restart systemd-networkd
```
