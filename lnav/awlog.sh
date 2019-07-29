#!/bin/bash

scriptPath="$( cd "$( dirname "$0" )" && pwd )"
baseDir="$( cd "$( dirname "$0" )" && cd ../.. && pwd )"

function usage {
  echo "----"
  echo "Usage: awlog [-i] [-u user] host" >&2
  exit 1
}

function ensureLnavInstalled {
  if ! brew ls --versions lnav >/dev/null 2>&1; then
    echo >&2 "lnav is required for enhanced log viewing, but it's not installed. Installing using brew..."
    brew install curl --with-libssh2
    brew install lnav --with-curl
    installLnavFormat
    echo "If dependencies installed correctly, please run this script again."
    
    echo "----"
    /bin/bash ${baseDir}/anywhere/metascripts/install.sh
    echo "Your metascript aliases have been updated to include \"awlog.\""

    usage
  fi
}

function installLnavFormat {
  lnav -i ${scriptPath}/anywhere-*.json
}

ensureLnavInstalled

user="c3a"
password="admin"
while getopts ":u:i" option; do
  case $option in
    u)
      user=${OPTARG}
      read -s -p "Password for user ${user}: " password
      ;;
    i)
      installLnavFormat
      usage
      ;;
    :)
      echo "ERROR: Option -$OPTARG requires an argument." >&2
      usage
      ;;
  esac
done

shift $((OPTIND-1))
host=$1
if [ -z "${host}" ]; then
  echo "ERROR: No host provided." >&2
  usage
fi

lnav sftp://${user}:${password}@${host}:/var/log/c3a/anywhere/anywhere.log
