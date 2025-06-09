package cafe.web.view;

import cafe.model.entity.Coffee;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Named
@RequestScoped
public class Cafe {

    private String baseUri;
    private Client client;

    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @PostConstruct
    private void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        baseUri = "http://localhost:9080" + request.getContextPath() + "/rest/coffees";
        client = ClientBuilder.newBuilder().build();
    }

    public List<Coffee> getCoffeeList() {
        return client.target(baseUri).path("/").request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Coffee>>() {
                });
    }

    public void addCoffee() throws IOException {
        Coffee coffee = new Coffee(name, price);
        client.target(baseUri).request(MediaType.APPLICATION_JSON).post(Entity.json(coffee));
        FacesContext.getCurrentInstance().getExternalContext().redirect("");
    }

    public void removeCoffee(String coffeeId) throws IOException {
        client.target(baseUri).path(coffeeId).request().delete();
        FacesContext.getCurrentInstance().getExternalContext().redirect("");
    }
}
