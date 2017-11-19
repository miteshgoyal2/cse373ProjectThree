package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages. If a webpage has many different
 * links to it, it should have a higher page rank. See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all available webpages.
     *
     * @param webpages
     *            A set of all webpages we have parsed.
     * @param decay
     *            Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon
     *            When the difference in page ranks is less then or equal to this number, stop iterating.
     * @param limit
     *            The maximum number of iterations we spend computing page rank. This value is meant as a safety valve
     *            to prevent us from infinite looping in case our page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        ISet<URI> empty = new ChainedHashSet<URI>();
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);
        IDictionary<URI, Double> linkDict = this.makeLinkDict(graph, webpages, empty);

        graph = this.updateGraph(graph, empty);
        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon, linkDict);
        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph, in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not* included within set of webpages you were
     * given. You should omit these links from your graph: we want the final graph we build to be entirely
     * "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {

        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<URI, ISet<URI>>();
        for (Webpage page : webpages) {
            // add URI to graph and its value = new HashSet
            graph.put(page.getUri(), new ChainedHashSet<URI>());
        }

        // get each page
        for (Webpage parentPage : webpages) {
            URI parentUri = parentPage.getUri();
            // get all URI this page links to
            IList<URI> links = parentPage.getLinks();
            // get each URI
            for (URI childUri : links) {
                // check within set and not links to itself
                if (graph.containsKey(childUri) && !parentUri.equals(childUri)) {
                    graph.get(childUri).add(parentUri);
                }
            }

        }
        return graph;

    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay
     *            Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon
     *            When the difference in page ranks is less then or equal to this number, stop iterating.
     * @param limit
     *            The maximum number of iterations we spend computing page rank. This value is meant as a safety valve
     *            to prevent us from infinite looping in case our page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph, double decay, int limit,
            double epsilon, IDictionary<URI, Double> linkDict) {

        double n = graph.size();
        double additiveTerm = (1.0 - decay) / n;
        IDictionary<URI, Double> pageRanks = new ChainedHashDictionary<URI, Double>();
        // Step 1: The initialize step should go here

        // System.out.println(epsilon);
        for (KVPair<URI, ISet<URI>> kvPair : graph) {
            double initialPageRank = 1.0 / graph.size() * 1.0;
            pageRanks.put(kvPair.getKey(), initialPageRank);
        }

        for (int i = 0; i < limit; i++) {
            IDictionary<URI, Double> temp = new ChainedHashDictionary<URI, Double>();

            // Step 2: The update step should go here
            for (KVPair<URI, ISet<URI>> pair : graph) {
                double sum = calculateSum(pair.getKey(), pageRanks, graph, linkDict);
                sum = sum * decay + additiveTerm;

                temp.put(pair.getKey(), sum);

            }
            // Step 3: the convergence step should go here.
            // Return early if we've converged.

            if (isConverged(temp, pageRanks, epsilon)) {
                break;
            } else {
                pageRanks = temp;
            }
        }

        return pageRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        // TODO: Add working code here
        return pageRanks.get(pageUri);
    }

    private double calculateSum(URI page, IDictionary<URI, Double> pageRank, IDictionary<URI, ISet<URI>> graph,
            IDictionary<URI, Double> linkDict) {

        double output = 0.0;
        for (URI uri : graph.get(page)) {
            double pageRankValue = pageRank.get(uri);
            output += pageRankValue / linkDict.get(uri);
        }
        return output;
    }

    private boolean isConverged(IDictionary<URI, Double> temp, IDictionary<URI, Double> pageRanks, double epsilon) {

        for (KVPair<URI, Double> pair : pageRanks) {
            if (Math.abs(temp.get(pair.getKey()) - pair.getValue()) > epsilon) {
                return false;
            }
        }
        return true;
    }

    private IDictionary<URI, Double> makeLinkDict(IDictionary<URI, ISet<URI>> graph, ISet<Webpage> webpages,
            ISet<URI> empty) {

        IDictionary<URI, Double> linkDict = new ChainedHashDictionary<URI, Double>();
        for (Webpage page : webpages) {
            URI parentUri = page.getUri();
            double size = page.getLinks().size();
            IList<URI> list = page.getLinks();
            if (size == 0) {
                empty.add(parentUri);
            }
            for (URI uri : list) {
                if (!graph.containsKey(uri) && parentUri.equals(uri)) {
                    size--;
                }
                if (size == 0) {
                    empty.add(parentUri);
                }
            }
            linkDict.put(parentUri, (size == 0 ? graph.size() : size));
        }

        return linkDict;
    }

    private IDictionary<URI, ISet<URI>> updateGraph(IDictionary<URI, ISet<URI>> graph, ISet<URI> empty) {

        for (KVPair<URI, ISet<URI>> pair : graph) {
            for (URI uri : empty) {
                graph.get(pair.getKey()).add(uri);
            }
        }
        return graph;
    }

}
