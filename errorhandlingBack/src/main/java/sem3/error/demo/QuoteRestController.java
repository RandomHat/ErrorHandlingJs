package sem3.error.demo;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("api/quotes")
@CrossOrigin
public class QuoteRestController {
  static Map<Integer, Quote> quotes;
  static {
    quotes = new HashMap<>();
    quotes.put(Quote.nextId, new Quote("I could agree with you but then we’d both be wrong","Harvey Specter"));
    quotes.put(Quote.nextId, new Quote("If you tell the truth you don’t have to remember anything","Mark Twain"));
    quotes.put(Quote.nextId, new Quote("Problems are not stop signs, they are guidelines","Harvey Specter"));
    quotes.put(Quote.nextId, new Quote("What we think, we become","Buddha"));
  }

  @GetMapping
  public Collection<Quote> getQuotes() {
     return quotes.values();
  }

  @GetMapping("/{id}")
  public Quote getQuote(@PathVariable int id) {
     Quote q = quotes.get(id);
     if(q==null){
       throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No quote with provided ID found");
     }
     return q;
  }

  @PostMapping
  public Quote addQuote(@RequestBody Quote body){
    if((body.getQuote()== null || body.getQuote().isBlank()) ){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A value for the Quote must be provided");
    }
    Quote newQuote = new Quote(body.getQuote(),body.getRef());
    quotes.put(newQuote.getId(),newQuote);
    return newQuote;
  }

  @PutMapping("/{id}")
  public Quote editQuote(@RequestBody Quote body,@PathVariable int id){
    Quote q = quotes.get(id);
    if(q==null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No quote with provided ID found");
    }
    if((body.getQuote()== null || body.getQuote().isBlank()) ){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A value for the Quote must be provided");
    }
    q.setQuote(body.getQuote());
    q.setRef(body.getRef());
    return q;
  }

  //Here it had been MUCH BETTER to define a DTO for the single value, in order to stick to JSON all over
  @PatchMapping("/{id}")
  public Quote updateQuoteText(@RequestBody String newText, @PathVariable int id){
    Quote q = quotes.get(id);
    if(q==null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No quote with provided ID found");
    }
    q.setQuote(newText);
    return q;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity delete(@PathVariable int id){
    Quote q = quotes.get(id);
    if(q==null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No quote with provided ID found");
    }
    quotes.remove(q.getId());
    return  ResponseEntity.status(HttpStatus.OK).build();
  }
}
