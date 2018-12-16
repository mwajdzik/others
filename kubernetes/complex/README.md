# Clean previous configs

kubectl get deployments
kubectl delete deployment client-deployment

kubectl get services
kubectl delete service client-node-port

# Apply new configurations from k8s directory
kubectl apply -f k8s

# Inspect
kubectl get pods -o wide
kubectl get services -o wide
kubectl get deployments -o wide

kubectl logs server-deployment-8789b9fd7-t6wrh

