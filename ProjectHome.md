**jRecruiter** is a show-case application for a variety of interesting Java related technologies. It is actively used by the [Atlanta Java Users Group](http://www.ajug.org) (AJUG) for their job posting service.

You can find the Maven Project site at: http://www.jrecruiter.org/

**2010-08-06** Setting up a dedicated Nexus Maven Repository: http://www.jrecruiter.org:8080/nexus/

**2009-09-15** AJUG presentation about jRecruiter: http://www.slideshare.net/hillert/jrecruiter-the-ajug-job-posting-service/

**2009-09-03** Migrated SVN data from Sourceforge to Google Code

|Hudson:                                      |http://www.jrecruiter.org:8080/hudson/job/jRecruiter/|
|:--------------------------------------------|:----------------------------------------------------|
|Sonar:                                          |http://www.jrecruiter.org:8080/sonar                    |
|Nexus Maven Repo for jRecruiter:                                      |http://www.jrecruiter.org:8080/nexus/                |
|Demo (Working on it):                                         |http://www.jrecruiter.org:8080/demo                    |
|AJUG Production Deployment:                  |http://www.ajug.org/jrecruiter/index.html            |

Installation Procedure

Pre-requisites:
  1. Please make sure you have access to a database, such as Postgresql
  1. Maven 2 (2.0.9 or higher)
  1. Checked out the source code from Subversion

Installation

While jRecruiter should run on a wide variety of servlet containers and databases, the main development is done using Tomcat 6 and Postgresql 8.x

Please make sure your servlet container is configured for SSL.

  1. Open a terminal and execute: **mvn clean package**
  1. Setup jrecruiter.properties under /opt/jrecruiter

**NOT complete yet**

&lt;wiki:gadget url="http://www.ohloh.net/p/14261/widgets/project\_basic\_stats.xml" height="220"  border="1" /&gt;

&lt;wiki:gadget url="http://www.ohloh.net/p/14261/widgets/project\_thin\_badge.xml" height="36"  border="0" /&gt;