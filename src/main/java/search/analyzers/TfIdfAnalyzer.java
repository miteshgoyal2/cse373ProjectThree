package search.analyzers;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;

import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    // This field contains the TF score for every single word in every document.
    private IDictionary<Webpage, IDictionary<String, Double>> tfScores;
    private IDictionary<URI, Double> normDocVector;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.tfScores = new ChainedHashDictionary<Webpage, IDictionary<String, Double>>();
        for (Webpage page : webpages) {
            IDictionary<String, Double> thisPageTfScores = this.computeTfScores(page.getWords());
            this.tfScores.put(page, thisPageTfScores);

        }

        IDictionary<String, Double> uniqueWords = this.allUniqueWords(webpages);

        this.idfScores = this.computeIdfScores(webpages, uniqueWords);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.normDocVector = this.getAllNormDocVector(documentTfIdfVectors);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: Feel free to change or modify these methods if you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * This method should return a dictionary mapping every single unique word found in any documents to their IDF
     * score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages, IDictionary<String, Double> uniqueWords) {

        IDictionary<String, Double> idfScoresVector = new ChainedHashDictionary<String, Double>();
        for (KVPair<String, Double> pairOfTermAndDocs : uniqueWords) {
            String word = pairOfTermAndDocs.getKey();
            double idfScore = Math.log(pages.size() / pairOfTermAndDocs.getValue());
            idfScoresVector.put(word, idfScore);
        }

        return idfScoresVector;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {

        // a dictionary containing a word and its TF score in a document
        IDictionary<String, Double> tfScoresForEachPage = new ChainedHashDictionary<String, Double>();

        // iterating through the document
        for (String word : words) {
            // first time see a word, tfScore = 1/size
            if (!tfScoresForEachPage.containsKey(word)) {
                tfScoresForEachPage.put(word, (double) (1.0 / words.size()));
            } else {
                // update tfScore each time the word is seen
                // newTfScore = oldTfScore + 1/size
                double currentTfScore = tfScoresForEachPage.get(word);
                double newTfScore = currentTfScore + (double) (1.0 / words.size());
                tfScoresForEachPage.put(word, newTfScore);
            }
        }

        return tfScoresForEachPage;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.

        IDictionary<URI, IDictionary<String, Double>> allDocumentTfIdfVectors;
        allDocumentTfIdfVectors = new ChainedHashDictionary<URI, IDictionary<String, Double>>();

        for (Webpage page : pages) {
            // create a dictionary mapping every unique word in this page to their TF-IDF score
            IDictionary<String, Double> tfIdfDictionary = new ChainedHashDictionary<String, Double>();
            // get TF score dictionary
            IDictionary<String, Double> tfScoresForThisPage = tfScores.get(page);
            // iterator through a list of words to get TF score for each word
            for (KVPair<String, Double> pairOfWordAndItsTfScore : tfScoresForThisPage) {
                // get IDF score for this word from idfScores dictionary
                double idfScoreForThisWord = idfScores.get(pairOfWordAndItsTfScore.getKey());
                // get TF score for this word
                double tfScoreForThisWordInThisPage = pairOfWordAndItsTfScore.getValue();
                // multiplying TF and IDF
                double tfIdfScoreForThisWordInThisPage = idfScoreForThisWord * tfScoreForThisWordInThisPage;
                // put in the dictionary created earlier just when enter this iteration
                tfIdfDictionary.put(pairOfWordAndItsTfScore.getKey(), tfIdfScoreForThisWordInThisPage);
            }

            // put in the output dictionary
            allDocumentTfIdfVectors.put(page.getUri(), tfIdfDictionary);
        }

        return allDocumentTfIdfVectors;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        // Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.

        IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = this.queryTfIdfScore(query);
        double numerator = 0.0;
        for (KVPair<String, Double> pairOfQuery : queryVector) {
            String word = pairOfQuery.getKey();
            double docWordScore = 0.0;
            if (documentVector.containsKey(word)) {
                docWordScore = documentVector.get(word);
            }
            double queryScore = queryVector.get(word);
            numerator += docWordScore * queryScore;
        }
        double denominator = normDocVector.get(pageUri) * norm(queryVector);

        if (denominator != 0) {
            return numerator / denominator;
        }
        return 0.0;
    }

    // helper methods
    // this method is responsible for finding all unique words in all documents
    private IDictionary<String, Double> allUniqueWords(ISet<Webpage> pages) {
        IDictionary<String, Double> uniqueWords = new ChainedHashDictionary<String, Double>();

        for (Webpage page : pages) {
            IDictionary<String, Double> tfScoreForThisPage = tfScores.get(page);
            for (KVPair<String, Double> pairOfWordAndTfScore : tfScoreForThisPage) {
                String word = pairOfWordAndTfScore.getKey();
                if (!uniqueWords.containsKey(word)) {
                    uniqueWords.put(word, 1.0);
                } else {
                    double numberOfDocsContainingTheWord = uniqueWords.get(word);

                    uniqueWords.put(word, numberOfDocsContainingTheWord + 1.0);
                }
            }
        }
        return uniqueWords;

    }

    private IDictionary<String, Double> queryTfIdfScore(IList<String> query) {
        IDictionary<String, Double> queryTfScore = this.computeTfScores(query);
        IDictionary<String, Double> queryTfIdfScore = new ChainedHashDictionary<String, Double>();

        for (String word : query) {
            double tfScoreForThisWord = queryTfScore.get(word);
            double idfScore = 0.0;
            if (idfScores.containsKey(word)) {
                idfScore = idfScores.get(word);
            }
            double tfIdfScore = tfScoreForThisWord * idfScore;
            queryTfIdfScore.put(word, tfIdfScore);
        }

        return queryTfIdfScore;
    }

    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair : vector) {
            double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }

    private IDictionary<URI, Double> getAllNormDocVector(
            IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors2) {
        IDictionary<URI, Double> output = new ChainedHashDictionary<URI, Double>();
        for (KVPair<URI, IDictionary<String, Double>> pair : documentTfIdfVectors2) {
            output.put(pair.getKey(), norm(pair.getValue()));
        }
        return output;
    }

}
