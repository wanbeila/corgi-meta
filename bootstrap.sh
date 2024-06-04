#!/bin/bash
set -e

TYPE=$1

KEY_SERVICE_LIST="service.list"
KEY_REGISTER_PARAM="register.param"
KEY_DB_DEFAULT="db.default"
KEY_REDIS_CONFIG="redis.config"
KEY_KAFKA_CONFIG="kafka.config"
KEY_ZOOKEEPER_CONFIG="zookeeper.config"
KEY_JVM_DEFAULT="jvm.default"
KEY_LOG_LEVEL="log.level"

function stopService() {
  INPUT_TYPE=$1
  SERVICE=""
  CFG_FILE=$2
  while IFS='=' read -r key value
      do
        # 通用配置
        PARAM_ARR=(${key//./ })
        if [ "${PARAM_ARR[0]}" = "$INPUT_TYPE" ]; then
          if [ "${PARAM_ARR[1]}" = "name" ]; then
            SERVICE=$value
            break 
          fi
        fi
      done < "$CFG_FILE"
      
  if [ -n "$SERVICE" ]; then
    TP_ID=`ps -ef|grep java|grep ${SERVICE}|grep -v kill|grep -v grep|awk '{print $2}'`
    if [ -n "$TP_ID" ]; then
      echo -e "\033[32mStopping ${SERVICE}\033[0m"
      ARR=${TP_ID//'\n'/}
      for AT_ID in ${ARR}; do
        echo "Kill Process [${AT_ID}] ${SERVICE} ..."
        kill -9 "${AT_ID}"
      done
      sleep 3
    fi
  fi
}

function startService() {
    SERVICE_NAME=$1
    CFG_FILE=$2
    PARENT_DIR=$3
    stopService "$SERVICE_NAME" "$CFG_FILE"
    register_value=""
    db_default=""
    redis_config=""
    kafka_config=""
    zookeeper_config=""
    jvm_default=""
    log_level=""
    
    s_name=""
    s_port=""
    s_param=""
    s_jvm=""
    s_db=""
    
    while IFS='=' read -r key value
      do
        # 通用配置
        if [ "$key" = "$KEY_REGISTER_PARAM" ]; then
          register_value=$value
        elif [ "$key" = "$KEY_DB_DEFAULT" ]; then
          db_default=$value
        elif [ "$key" = "$KEY_REDIS_CONFIG" ]; then
          redis_config=$value
        elif [ "$key" = "$KEY_KAFKA_CONFIG" ]; then
          kafka_config=$value
        elif [ "$key" = "$KEY_ZOOKEEPER_CONFIG" ]; then
          zookeeper_config=$value
        elif [ "$key" = "$KEY_JVM_DEFAULT" ]; then
          jvm_default=$value
        elif [ "$key" = "$KEY_LOG_LEVEL" ]; then
          log_level=$value
        else
          PARAM_ARR=(${key//./ })
          if [ "${PARAM_ARR[0]}" = "$SERVICE_NAME" ]; then
            if [ "${PARAM_ARR[1]}" = "name" ]; then 
              s_name=$value
            elif [ "${PARAM_ARR[1]}" = "jvm" ]; then
              s_jvm=$value
            elif [ "${PARAM_ARR[1]}" = "db" ]; then
              s_db=$value
            elif [ "${PARAM_ARR[1]}" = "port" ]; then
              s_port=$value
            elif [ "${PARAM_ARR[1]}" = "param" ]; then
              s_param=$value
            fi    
          fi
        fi
      done < "$CFG_FILE"
    
    if [ ! "$s_name" ]; then
      echo "服务配置$SERVICE_NAME不存在"
      exit 1
    fi
    
    if [ ! "$s_port" ]; then
      echo "服务$SERVICE_NAME端口未配置"
      exit 1
    fi
    
    if [ "$s_jvm" ]; then 
      jvm_default=$s_jvm
    fi
    
    if [ "$s_db" ]; then
      db_default=$s_db
    fi

    CMD="nohup java ${jvm_default} -jar ./${s_name}.jar --SERVER_PORT=${s_port} ${register_value} ${db_default} ${redis_config} ${kafka_config} ${zookeeper_config} --LOG_LEVEL=${log_level}"

    echo "service_name=$SERVICE_NAME"
    if [ $SERVICE_NAME = "stat" ]; then
       CMD="nohup java ${jvm_default} -Dloader.path=stat_plugin/ -jar ./${s_name}.jar --SERVER_PORT=${s_port} ${register_value} ${db_default} ${redis_config} ${kafka_config} --LOG_LEVEL=${log_level}"
    fi

    if [ "${s_param}" ]; then
      CMD="${CMD} ${s_param}"
    fi
    CMD="${CMD} > /dev/null 2>&1 &"
    
    echo -e "\033[32mStarting ${s_name}\033[0m"
    echo "${CMD}"
    cd ../
    eval "${CMD}"
    sleep 3
    NEW_ID=`ps -ef|grep java|grep ${s_name}|grep -v kill|grep -v grep|grep -v bootstrap.sh|awk '{print $2}'`
    echo "${NEW_ID}"

}

CUR_DIR=$(dirname "$0")
cd "${CUR_DIR}"
CUR_DIR=$(pwd)
CFG_FILE="${CUR_DIR}"/config.properties
PARENT_DIR=$(dirname "$CUR_DIR")

if [ -f "$CFG_FILE" ]; then 
  if [ "${TYPE}" = "list" ]; then
    echo "loading $CFG_FILE"
    while IFS='=' read -r key value
    do
      if [ "$key" = "$KEY_SERVICE_LIST" ]; then
        echo "$value"
        break
      fi
    done < "$CFG_FILE"
  elif [ "${TYPE}" = "start" ]; then
    echo "loading $CFG_FILE"
    SERVICE_NAME_STRING=$2
    SERVICE_NAME=(${SERVICE_NAME_STRING//,/ })
    for var in "${SERVICE_NAME[@]}"
    do
       startService "$var" "$CFG_FILE" "$PARENT_DIR"
    done
  elif [ "${TYPE}" = "stop" ]; then
    SERVICE_NAME_STRING=$2
    SERVICE_NAME=(${SERVICE_NAME_STRING//,/ })
    for var in "${SERVICE_NAME[@]}"
    do
       stopService "$var" "$CFG_FILE" "$PARENT_DIR"
    done
  else
    echo "不支持的操作，请使用 bash bootstrap.sh start <服务名>"
  fi
fi
