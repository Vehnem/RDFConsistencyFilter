# RDFConsistencyFilter

A RDF filter which consumes RDF Data from a SPARQL Endpoint with a CONSTRUCT-Query
Full webinterface frontend

## Docker

Building docker image

    $ git clone git@github.com:Vehnem/RDFConsistencyFilter.git
    $ cd RDFConsistencyFilter/docker
    $ docker build -t #{Name}

After build

    $ docker run -d --name RDF-CF -p 80:8080 #{name}
    
Then visit you domain under port 80

## Manually (naiv)
    $ mkdir #{app_folder}
    $ cd #{app_folder}
    $ git clone git@github.com:Vehnem/RDFConsistencyFilter.git
    $ cd RDFConsistencyFilter/
    $ mvn package
    $ cd ../
    $
    $ java -jar target/RDFConsistencyFilter-0.0.1-SNAPSHOT.jar

Then visit localhost:8080

## Frameworks

listed in pom file and [RDFUnit](https://github.com/AKSW/RDFUnit/) 