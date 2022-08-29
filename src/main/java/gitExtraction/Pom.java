package gitExtraction;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.eclipse.jgit.lib.ObjectLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Pom {

    static void getDependencyManagement(Model model, Map<String, Dependency> depsMap) throws IOException {
        DependencyManagement dependencyManagement =  model.getDependencyManagement();

        if ( dependencyManagement != null ) {

            String techno = "Technology: ";

            for (Dependency dependency : dependencyManagement.getDependencies()) {
                depsMap.put(dependency.getManagementKey(), dependency);

                techno = techno + dependency.getArtifactId() + ", ";

                //System.out.println("Version: " + properties.getProperty(dependency.getArtifactId())) ;
            }
            techno = techno.substring(0, techno.length() - 2);

            Cypher.writeFile(techno);
            System.out.println(techno);


        }
    }

    public static void getProperties(Model model) {

        Properties properties = model.getProperties();

        properties.list(System.out);

    }

    public static void getDependenciesList(Model model, Map<String, Dependency> depsMap) {
        List<Dependency> listOfDependencies = model.getDependencies();

        if ( listOfDependencies != null )
        {
            for ( Dependency dependency : listOfDependencies )
            {
                depsMap.put( dependency.getManagementKey(), dependency );

                System.out.println("Technology: " + dependency.getArtifactId() + " & Version: " + dependency.getVersion()) ;

            }
        }
    }

    public static void readPom(ObjectLoader loader) throws IOException {
        loader.copyTo(System.out);
    }
}
