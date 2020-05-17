FROM maven:3.6.0-jdk-8

RUN apt-get update -y -qq && apt upgrade -y -qq && apt-get install git apt-transport-https ca-certificates curl software-properties-common gnupg2 -y -qq && curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" && apt update -y -qq && apt install docker-ce -y -qq && mkdir -p /opt/snoops/ && cd /opt/snoops/ && git clone https://github.com/kryptonmlt/snoops.git . && mvn clean install

CMD cd /opt/snoops/target && java -jar `find .  -printf "%f\n" | grep .jar$`