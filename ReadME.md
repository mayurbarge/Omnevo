# World Of Music

Running the program
1. Create input directory
create a folder in-xml where jar is located
2. Add file worldofmusic.xml inside this folder
3. Create ouput directory 
create a folder out-xml where jar is located
4. Run following command
java -jar Omnevo-assembly-1.0-SNAPSHOT.jar com.worldofmusic.main.Main
5. The output file will be generated inside out-xml

###Example
#### Omnevo > ls -lrt
#### Omnevo >
#### Omnevo-assembly-1.0-SNAPSHOT.jar application                      domain                           out-xml                          repository                       xmlservices
#### ReadME.md                        build.sbt                        in-xml                           project                          target
#### Omnevo > java -jar Omnevo-assembly-1.0-SNAPSHOT.jar com.worldofmusic.main.Main
### Sample outputs from above execution
#### Output file saved successfully. [When file written successfully]
#### Error while writing output file. out-xml/out.xml [No such file or directory) [when out-xml is not created]


# Design considerations
The design follows modular monolith where each component is separated with scala module.
1. domain module - abstracts away domain objects
2. repository module - abstracts away Sink and Sources for XML
3. xmlservices module - maps domain to and from xml
4. application module - Final place where effects are composed
The domain remains pure, abstracted from the technical aspects and represents business behaviour.

MusicMozRecord is the current domain object which can be modified in future to adjust according to customer demands.
When structure of MusicMozRecord change only corresponding domain and implicit recordsParser which converts
Node to MusicMozRecord gets changed, rest code remains intact.

When data source changes from XML file to online service corresponding SoureRepository needs to be modified.
New rules for filtering records can be easily added to FilterRules class in domain module

# Time tracking
Domain modelling and initial domain design - 3 hr
Writing XML parsers and serializers - 5 hr
Testing tagless final classes with MonadErrors - 3 hr
Testcases preparation - 4 hr
Repository layer - 2 hr
Transofrmer to map xml to domain - 2 hr
Assembly and jar building and writing ReadMe.md - 5 hours 