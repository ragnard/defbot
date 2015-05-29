FROM clojure

RUN mkdir /opt/defbot

WORKDIR /opt/defbot

COPY . /opt/defbot

RUN lein uberjar

ENTRYPOINT ["java", "-jar", "target/defbot.jar"]