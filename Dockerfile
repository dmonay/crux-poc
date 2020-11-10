FROM clojure
COPY . /usr/src/app
WORKDIR /usr/src/app
CMD ["clojure", "-A:uberjar"]
CMD ["java", "-jar", "banking-on-clojure.jar"]