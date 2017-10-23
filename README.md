# infinitum-svn-search

[![Build Status](https://travis-ci.org/swoeste/infinitum-svn-search.svg?branch=master)](https://travis-ci.org/swoeste/infinitum-svn-search)
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=de.swoeste.infinitum:infinitum-svn-search-aggr)](https://sonarcloud.io/dashboard/index/de.swoeste.infinitum:infinitum-svn-search-aggr)

Configuration ...
------------------

The configuration file is located at %ROOT%/conf/`svnsearch.properties`.

    # Binding IP address. For servers with more than one IP address, this property specifies which
    # address will be used for listening on the specified ports.
    # By default, ports will be used on all IP addresses associated with the server.
    infinitum.web.host = 0.0.0.0
  
    # SVN Search listening port.
    # The default value is 80.
    infinitum.web.port = 80
  
    # The web context must start with a forward slash (for example /svnsearch).
    # The default value is root context ('/'). 
    infinitum.web.context = /
  
    # The maximum amount of search results.
    # The default value is 100.
    svn.index.maxSearchResults = 100
  
    # The amount of search results displayed on a page.
    # The default value is 10.
    svn.index.searchResultsPerPage = 10

    # The path the local index should be stored.
    # For example: D:/svnsearch/data/index
    svn.index.path = 

    # The address of the svn repository which should be indexed.
    # For example: https://svn-search.googlecode.com/svn/
    svn.index.repository = 

Something like ...
------------------
During the developement of this project I found some projects with similar goals.
* SVN Searcher (http://sourceforge.net/projects/svn-search/)
* SVN Query (http://svnquery.tigris.org/)
* Atlassian FishEye (https://www.atlassian.com/software/fisheye/overview)
