FROM clojure

RUN mkdir /opt/defbot

WORKDIR /opt/defbot

COPY . /opt/defbot

RUN lein uberjar

ENTRYPOINT ["java", "-Djava.security.policy=security.policy", "-jar", "target/defbot.jar"]