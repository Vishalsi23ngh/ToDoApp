package com.example.toDoAppBackend.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.Set;

public class MyAnalyzer extends Analyzer {

    private  final CharArraySet stopWords;
    private  final Set<String> protectedTerms;

    public MyAnalyzer(Set<String> stopWords, Set<String> protectedTerms) {
        this.stopWords = CharArraySet.copy(stopWords);
        this.protectedTerms = protectedTerms;
    }

    @Override
    protected TokenStreamComponents createComponents(String s) {
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        TokenStream tokenStream = new StopFilter(tokenizer,stopWords);
        tokenStream = new PorterStemFilter(tokenStream);
        return  new TokenStreamComponents(tokenizer, tokenStream);
    }

    public  String stem(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        StringBuilder result;
        try (TokenStream tokenStream = tokenStream(null, text)) {
            result = new StringBuilder();
            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);

            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                result.append(charTermAttribute.toString()).append(" ");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }
}
