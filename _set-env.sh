if [ -z "$DOCKER_HOST_IP" ] ; then
    if [ -z "$DOCKER_HOST" ] ; then
      export DOCKER_HOST_IP=`hostname`
    else
      echo using ${DOCKER_HOST?}
      XX=${DOCKER_HOST%\:*}
      export DOCKER_HOST_IP=${XX#tcp\:\/\/}
    fi
fi

echo DOCKER_HOST_IP is $DOCKER_HOST_IP


export EVENTUATELOCAL_KAFKA_BOOTSTRAP_SERVERS=$DOCKER_HOST_IP:9092
export EVENTUATELOCAL_ZOOKEEPER_CONNECTION_STRING=$DOCKER_HOST_IP:2181
export CDC_SERVICE_URL=http://$DOCKER_HOST_IP:8099
export SPRING_DATA_MONGODB_URI=mongodb://${DOCKER_HOST_IP?}/customers_orders

