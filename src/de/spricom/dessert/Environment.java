package de.spricom.dessert;

import java.io.IOException;

import de.spricom.dessert.dependency.DependencyAssert;
import de.spricom.dessert.dependency.Repository;
import de.spricom.dessert.dependency.Rule;
import de.spricom.dessert.traversal.PathProcessor;

public class Environment {
    private Repository repository = new Repository();
    
    public Environment() throws IOException {
        this(null);
    }
    
    public Environment(String classpath) throws IOException {
        PathProcessor proc = new PathProcessor();
        if (classpath != null) {
            proc.setPath(classpath);
        }
        proc.traverseAllClasses(repository);
    }
    
    public DependencyAssert assertThat(Rule rule) {
        return new DependencyAssert(repository, repository.getDependencyUnit(rule));
    }
}
