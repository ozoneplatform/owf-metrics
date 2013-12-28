eventCreateWarStart = { name, stagingDir ->
  println "making owf-all.jar"

      //jar up all files in WEB-INF/classes and put into WEB-INF/lib/owf-all.jar
      ant.jar(destfile:"${stagingDir}/WEB-INF/lib/owf-all.jar", update:false) {
        fileset(dir:"${stagingDir}/WEB-INF/classes") {
          exclude(name:"**/gsp_*.*")
          exclude(name:"**/*.properties")
          exclude(name:"**/*.xml")
          exclude(name:"**/.gitignore")
        }
      }

      ant.delete(includeemptydirs:true) {
        fileset(dir:"${stagingDir}/WEB-INF/classes") {
          exclude(name:"**/gsp_*.*")
          exclude(name:"**/*.properties")
          exclude(name:"**/*.xml")
        }
      }

  println "finished making owf-all.jar"
}

