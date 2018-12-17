# Clean previous configs

kubectl get deployments
kubectl delete deployment client-deployment

kubectl get services
kubectl delete service client-node-port

# Apply new configurations from k8s directory

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/mandatory.yaml

minikube addons enable ingress

kubectl apply -f k8s

# Inspect
kubectl get pods -o wide
kubectl get services -o wide
kubectl get deployments -o wide

kubectl get pvc -o wide
kubectl get pv -o wide

kubectl logs server-deployment-8789b9fd7-t6wrh

# Secret (created with imperative command)

kubectl create secret generic pgpassword --from-literal PGPASSWORD=password

kubectl get secrets

# Dashboard

minikube dashboard
