package gitExtraction;

import java.io.*;
import java.nio.file.Files;

import java.util.*;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import static gitExtraction.Cypher.myFile;
import static gitExtraction.Cypher.myWriter;

public class Main {

    public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException, IOException, XmlPullParserException {
        System.out.println("Welcome gitExtraction");

        String uri, username;
        char[] password;
        UsernamePasswordCredentialsProvider credential;

        //create temporary repository
        File localPath = Repository.createTemporaryRepository();
        Files.delete(localPath.toPath());

        Cypher myobj = new Cypher(myFile);
        Cypher.writeFile("toto");


/*
        //check arg
        switch (args.length) {
            case 1:
                //Single repository
                uri = UserConsole.setURI();
                System.out.println("Connecting to repository: " + uri);
                System.out.println("To continue you need a token (security Access) from https://www.europarl.europa.eu/portalep/vdi.html");
                username = UserConsole.setUser();
                password = UserConsole.setPassword();
                credential = new UsernamePasswordCredentialsProvider(username, password);
                gitExtract(uri,localPath,credential);
                break;

            case 0:
                //Multiple URI from file parameter1

                String path="C://Users//bde_v//OneDrive//Bureau//EP//neo4j//CADeNAS//gitExtraction_repository_list.txt";
                ArrayList<String> repoList = Repository.getListRepo(path);
                System.out.println("To continue you need a token (security Access) from https://www.europarl.europa.eu/portalep/vdi.html");

                //username = UserConsole.setUser();
                //password = UserConsole.setPassword();

                credential = new UsernamePasswordCredentialsProvider("vcourtois", "45Courcelles");
                for (String s : repoList) {
                    gitExtract(s, localPath, credential);
                }

        }

*/
        System.out.println("Job is done bye gitExtraction");
    }


    public static void gitExtract(String URI, File localPath, UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider) throws GitAPIException, IOException, XmlPullParserException {


        Git git = Repository.CloneRepository(URI, localPath, usernamePasswordCredentialsProvider);

        git.open(localPath).checkout();

        org.eclipse.jgit.lib.Repository repository = git.getRepository();

        //search and accessing file
        //find HEAD

        ObjectId lastCommitId = repository.resolve(Constants.HEAD);

        // RevWalk allows to walk over commits based on some filtering that is defined
        try (RevWalk revWalk = new RevWalk(repository)) {

            RevCommit commit = revWalk.parseCommit(lastCommitId);

            // and using commit's tree find the path
            RevTree tree = commit.getTree();

            System.out.println("Having tree: " + tree);


            //find specific file
            try (TreeWalk treeWalk = new TreeWalk(repository)) {

                treeWalk.addTree(tree);

                treeWalk.setRecursive(false);

                treeWalk.setFilter(PathFilter.create("pom.xml"));

                if (!treeWalk.next()) {

                    throw new IllegalStateException("Did not find pom.xml");
                }

                ObjectId ObjectId = treeWalk.getObjectId(0);

                ObjectLoader loader = repository.open(ObjectId);

                //read file
                Pom.readPom(loader);


                //check properties for CADENAS
                System.out.println("");

                System.out.println("////////////////////////////////////////// CADENAS ////////////////////////////////////////// ");

                MavenXpp3Reader reader = new MavenXpp3Reader();

                Model model = reader.read(loader.openStream());

                System.out.println("Project " + model.getId());

                System.out.println("////////////////////////////////////////// ");

                Map<String, Dependency> depsMap = new LinkedHashMap<>();

                Pom.getDependenciesList(model, depsMap);

                Pom.getDependencyManagement(model, depsMap);

                Pom.getProperties(model);

                System.out.println("////////////////////////////////////////// CADENAS ////////////////////////////////////////// ");

            }

            revWalk.dispose();
        } catch (IllegalStateException e) {
            System.out.println("Could not find pom.xml");
        } catch (RepositoryNotFoundException e) {
            System.out.println("Repository does not exist");
        }
        git.close();

        Repository.deleteTemporaryRepository(localPath);
    }

}