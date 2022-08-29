package gitExtraction;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jetbrains.annotations.NotNull;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;


public abstract class Repository {

    public static Git CloneRepository(@NotNull String MyUri,@NotNull File Directory,@NotNull UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider) throws GitAPIException {
        try {

            Git git = Git.cloneRepository()
                    .setURI(MyUri)
                    .setCredentialsProvider(usernamePasswordCredentialsProvider)
                    .setDirectory(Directory)
                    .call();

            System.out.println("Created repository: " + git.getRepository().getDirectory());

            return git;

        } catch (TransportException t) {

            System.out.println(t.getMessage());
        }

        return null;
    }

    public static File createTemporaryRepository() throws IOException {

        File localPath = File.createTempFile("GitExtraction_tempo", "");

        return localPath;
    }

    public static void deleteTemporaryRepository(File localPath) throws IOException {

        //delete temporary repository
        System.out.println("Delete repository" + localPath.toPath());
        deleteDirectoryStream(localPath.toPath());
    }

    static void deleteDirectoryStream(Path path) throws IOException {

        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static ArrayList<String> getListRepo(String pathname) throws FileNotFoundException {

        ArrayList<String> listRepo = new ArrayList<String>();

        File listRepository = new File(pathname);

        Scanner myReader = new Scanner(listRepository);

        while (myReader.hasNextLine()) {

            listRepo.add(myReader.nextLine());

        }

        return listRepo;
    }


}
