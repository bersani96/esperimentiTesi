#!/bin/bash

source "/home/vagrant/provisioning/kubernetes/scripts/common.sh"

#
# copia i file HOSTS.CONF e DAEMON.JSON per sovrascrivere la configurazione di docker 
# - il primo, per abilitare la comunicazione remota del nodo 
# - il secondo, per ulteriori configurazioni (ad es., per abilitare eventuali registry insicuri) (non usato) 
# 

# copia /etc/systemd/system/docker.service.d/hosts.conf
mkdir -p /etc/systemd/system/docker.service.d
cp ${RESOURCES}/docker.service.d/hosts.conf /etc/systemd/system/docker.service.d/hosts.conf
chmod a-x /etc/systemd/system/docker.service.d/hosts.conf

# copia /etc/docker/daemon.json (NO, ATTUALMENTE NON USATO)
#mkdir -p /etc/docker
#cp ${ASW_RESOURCES}/etc.docker/daemon.json /etc/docker/daemon.json
#chmod a-x /etc/docker/daemon.json