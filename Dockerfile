FROM java:8

MAINTAINER ashtwentyfour

WORKDIR /code

ADD src /code/src
ADD bin /code/bin
ADD test_cases /code/test_cases

RUN javac -d /code/bin -sourcepath /code/src /code/src/json_parser/*.java
CMD ["java", "-cp", "/code/bin", "json_parser.Driver", "/code/test_cases/sample_json.json"]
