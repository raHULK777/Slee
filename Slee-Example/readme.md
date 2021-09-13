http://developer.opencloud.com/maven2/public/

Download maven OpenCloud jainslee plugin and run below command to install locally. The same plugin is available under lib folder.

command : mvn install:install-file -Dfile=lib/maven-opencloud-jainslee-plugin-0.9.jar -DgroupId=com.opencloud.maven.plugins -DartifactId=maven-opencloud-jainslee-plugin -Dversion=0.9 -Dpackaging=jar -DgeneratePom=true

command : mvn install:install-file -Dfile=lib/slee.jar -DgroupId=OpenCloud -DartifactId=slee -Dversion=1.1 -Dpackaging=jar -DgeneratePom=true

command: mvn install:install-file -Dfile=lib/jsip-library-1.2.jar -DgroupId=javax.sip -DartifactId=jsip-library -Dversion=1.2 -Dpackaging=jar -DgeneratePom=true

command: mvn install:install-file -Dfile=lib/jsip-events-1.2.jar -DgroupId=javax.sip -DartifactId=jsip-events -Dversion=1.2 -Dpackaging=jar -DgeneratePom=true

command: mvn install:install-file -Dfile=lib/jsip-ratype-1.2.jar -DgroupId=javax.sip -DartifactId=jsip-ratype -Dversion=1.2 -Dpackaging=jar -DgeneratePom=true

command: mvn install:install-file -Dfile=lib/ocsip-ratype-2.2.jar -DgroupId=OpenCloud -DartifactId=ocsip-ratype -Dversion=2.2 -Dpackaging=jar -DgeneratePom=true

command: mvn install:install-file -Dfile=lib/ocsip-ratype-classes-2.2.jar -DgroupId=OpenCloud -DartifactId=ocsip-ratype-classes -Dversion=2.2 -Dpackaging=jar -DgeneratePom=true
