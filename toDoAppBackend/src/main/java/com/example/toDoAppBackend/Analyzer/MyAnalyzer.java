package com.example.toDoAppBackend.Analyzer;

import com.example.toDoAppBackend.util.StemmerHelper;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

@Component
public class MyAnalyzer extends Analyzer {

    private  final CharArraySet stopWords;
    private  final Set<String> protectedTerms;

    public MyAnalyzer(Set<String> stopWords, Set<String> protectedTermsList) {
        this.stopWords = new  CharArraySet(stopWords,true);
        this.protectedTerms = new HashSet<>(protectedTermsList);
    }

    @Override
    protected TokenStreamComponents createComponents(String s) {
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        TokenStream tokenStream = new StopFilter(tokenizer,stopWords);
        tokenStream = new TokenFilter(tokenStream) {
            @Override
            public boolean incrementToken() throws IOException {
                if(!input.incrementToken()){
                    return  false;
                }
                CharTermAttribute termAttribute = getAttribute(CharTermAttribute.class);
                String term = termAttribute.toString();
                if(protectedTerms.contains(term)){
                    return  true;
                }
                return  true;
            }
        };
        tokenStream = new PorterStemFilter(tokenStream);
        return  new TokenStreamComponents(tokenizer, tokenStream);
    }

    public  String stem(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }

        Pair<String, String> placeHolders = StemmerHelper.getPlaceHolders(text,protectedTerms);
        System.out.println(text);
        text = placeHolders.getFirst();
        try (TokenStream tokenStream = tokenStream(null, new StringReader(text))) {
            StringBuilder result = new StringBuilder();
            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);

            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                result.append(charTermAttribute.toString()).append(" ");
            }
            tokenStream.end();
            String stemmedText = result.toString();
            if(placeHolders.getSecond() != null){
                stemmedText = text.replace("_PLACEHOLDER_",placeHolders.getSecond());
            }
            System.out.println();
            return  stemmedText.trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
