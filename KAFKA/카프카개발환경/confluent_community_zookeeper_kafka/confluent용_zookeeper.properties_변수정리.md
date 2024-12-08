
### /etc/kafka/zookeeper.properties에 기반함

```markdown
image: confluentinc/cp-zookeeper:latest
```
ZOOKEEPER_DATADIR=/var/lib/zookeeper
ZOOKEEPER_CLIENTPORT=2181
ZOOKEEPER_MAXCLIENTCNXNS=0
ZOOKEEPER_ADMIN_ENABLESERVER=false
# ZOOKEEPER_ADMIN_SERVERPORT=8080


