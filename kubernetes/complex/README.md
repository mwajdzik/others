# Clean previous configs

kubectl get deployments
kubectl delete deployment client-deployment

kubectl get services
kubectl delete service client-node-port

# Apply new configurations from k8s directory
kubectl apply -f k8s

# Inspect
kubectl get pods
kubectl get services
kubectl get deployments

