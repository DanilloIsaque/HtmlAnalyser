import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class HtmlAnalyzer {
    public static void main(String[] args) throws Exception {
        
        String url = args[0]; // recebe o link 
       // System.out.println(url);
       
       try {
            String lines = Connection(url);
            //System.out.println(lines);
                
            List<String> htmlList = new ArrayList<>();
                for (String line : lines.split("\n")) {   // separa o html sempre que houver alguma linha
                    line = line.trim();
                    if (!line.isEmpty()) {
                        htmlList.add(line);
                    }
                }

            // System.out.println(htmlList);
            String retorno = ExtractText(htmlList);
            System.out.println(retorno);

        }catch(Exception e){
            System.out.println("URL connection error");
        }

    }

    private static String Connection (String urlSite) throws Exception{ // requisição http para pegar o conteudo da pagina
        
        URL url = new URL(urlSite);
        Scanner scanner = new Scanner(url.openStream()); // ler o conteudo da pagina e usado para percorrer linha por linha
        StringBuilder html = new StringBuilder();

        while (scanner.hasNextLine()) { 
            html.append(scanner.nextLine()).append("\n"); // enquanto houver linha, 
                                                            //ele vai ler e manter a formatação original no string builder 
                                                            //para retornar ele completo como string
        }
        scanner.close();
        return html.toString(); // 
    }
    

    private static String ExtractText( List<String> htmlList){

        int countDepths = 0;
        int currentMaxDepth = 0;
        String text = null;
        
        for(String line : htmlList){
            if(line.startsWith("<") && line.endsWith(">")){

                if (!line.startsWith("</")) {
                    countDepths++;
                } else if (!line.endsWith("/>")){ // caso seja um /br, por exemplo
                    countDepths--;
                }
            } else { // senao for uma tag, entra aqui e, no caso, seria conteudo

                if (countDepths > currentMaxDepth) {
                    currentMaxDepth = countDepths;
                    text = line;
                }
            }
        }

        if(text != null && countDepths == 0){ // 0 para mostrar q as tags foram abertas e fechadas corretamente
            return text;
        }else{
            return "Malformed HTML";
        }

    }
}
