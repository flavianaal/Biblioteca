package io.programe.converter;



import io.programe.modelo.Livro;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "livroconverter", forClass = Livro.class) // Nome pelo qual será referenciado
public class LivroConverter implements Converter {

   @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            // Tenta converter o valor do String para Funcionario usando o atributo
            return (Livro) uiComponent.getAttributes().get(value);
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {

        if (value instanceof Livro) {

            Livro entity = (Livro) value;

            if (entity != null && entity instanceof Livro && entity.getId() != null) {
                // Armazena o Funcionario no atributo para uso posterior
                uiComponent.getAttributes().put(entity.getId().toString(), entity);
                return entity.getId().toString();
            }
        }
        return "";
    }

}
