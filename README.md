# Play! Framework Tutorials

## Installation

Go to http://www.playframework.org/download and download the latest Play! Release.
Unzip the archive and add the play command to you path.
[More Information about Setup here](http://www.playframework.org/documentation/1.2.3/install)

## Examples

The apps are configured to run with a mysql Database. You can always change the Database by editing app/conf/application.conf.

db=mem for in-Memory Database.
db=fs for a file based Database (H2).

### bookmarks

This is a simple bookmarks listing application. You start this example with *play run* in the bookmarks directory.

### vcards

This is an app for parsing vcards to a Database. The app will also support export to VCard Format in the future.

### logr

This should demonstrate some simple Multidimensional Database Operations. The Use-Case is, to import different Log-Files (Apache2, MySql e.g.) and plott them towards date/host/type and count()/sum().
## Links

- You can find more about these Tutorials [in my blog](http://www.philipp.haussleiter.de/category/play-framework/).
- The [Play! Documentation](http://www.playframework.org/documentation/1.2.3/home) is also always nice to know.
- There is also a [handy Cheat Sheet](www.crionics.com/public/play-contrib/TheUltimatePlayCheatSheet.pdf).