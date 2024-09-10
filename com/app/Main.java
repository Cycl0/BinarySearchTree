import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("unchecked")
class Main {
  public static <T extends Comparable<T>> void main(String[] args) {

    // Define tipos de dados disponíveis
    Class<?>[] classes = {Integer.class, Float.class, Double.class};
    Class<T> dataTypeClass = (Class<T>) chooseDataType(classes);
    List<T> valueList = promptValueList(dataTypeClass);
    BinarySearchTree<T> bst = new BinarySearchTree<>(valueList);
    bst.balanceTree();
    bst.print();
            // Flag para sair
            boolean exit = false;

            // Loop principal
            while (!exit) {

                // Pegar metodos da classe BinarySearchTree
                Method[] methods = BinarySearchTree.class.getMethods();

                int methodOption;

                // Loop de validacao
                while (true) {
                    try {
                        System.out.println("Escolha o metodo:");

                        for (int i = 0; i < 16; ++i) { // Limita a 16 metodos
                            Method method = methods[i];
                            String methodName = method.getName();
                            Parameter[] parameters = method.getParameters();

                            StringBuilder params = new StringBuilder();
                            for (int j = 0; j < parameters.length; ++j) {
                                if (j > 0) {
                                    params.append(", ");
                                }
                                params.append(parameters[j].getType().getSimpleName());
                            }

                            // Mostra os metodos com os parametros
                            System.out.println((i + 1) + " - " + methodName + "(" + params.toString() + ")");
                        }

                        System.out.println("0 - Exit");
                        System.out.print("> ");

                        // Numero da opcao
                        methodOption = Integer.parseInt(scanner.nextLine());

                        // Valida a opcao escolhida
                        if (methodOption == 0) {
                            System.out.println("\nSaindo...");
                            exit = true;
                            break;
                        } else if (methodOption < 1 || methodOption > methods.length) {
                            System.out.println("\nOpcao invalida. Escolha um numero entre 1 e " + methods.length + ".\n");
                        } else {
                            // Opcao valida quebra o loop
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nEntrada invalida. Por favor digite um numero inteiro valido.\n");
                    }
                }

                if (exit) {
                    break;
                }

                // Pega o metodo escolhido
                Method chosenMethod = methods[methodOption - 1];
                Parameter[] params = chosenMethod.getParameters();
                Object[] argsForMethod = new Object[params.length];

                // Solicita argumentos para metodos que os requerem
              for (int i = 0; i < params.length; ++i) {
                  Parameter param = params[i];
                  Class<?> paramType = param.getType();
                  System.out.print(paramType.getSimpleName() + ": ");
                  if (paramType.equals(int.class)) {
                      argsForMethod[i] = Integer.parseInt(scanner.nextLine());
                  } else if (paramType.equals(BinarySearchTree.Node.class)) {
                      String nodeString = scanner.nextLine();
                      argsForMethod[i] = bst.findNodeByString(nodeString);
                  } else if (Comparable.class.isAssignableFrom(paramType)) {
                      argsForMethod[i] = promptCreateType(dataTypeClass);
                  }
              }

              System.out.println("\n         __ Print __         \n");

                try {
                    // Garante que bst nao e nulo antes de invocar o metodo
                    if (bst == null) {
                        System.out.println("Instancia de BinarySearchTree nao inicializada.");
                        continue;
                    }

                    // Invoca o metodo com os argumentos
                    Object outputStr = chosenMethod.invoke(bst, argsForMethod);
                    // Imprime a saida
                    System.out.println("\n........................SAIDA.......................");
                    System.out.println(outputStr);
                    bst.balanceTree();
                    bst.print();
                    System.out.println(".....................................................\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            scanner.close();
        }

        private static Scanner scanner = new Scanner(System.in);

        // escolher o tipo de dados
        private static Class<?> chooseDataType(Class<?>[] dataStructClassList) {
            System.out.println("Escolha um dos seguintes tipos de dados: ");
            for (int i = 0; i < dataStructClassList.length; ++i) {
                System.out.println((i + 1) + " - " + dataStructClassList[i].getSimpleName());
            }
            int option = -1;
            while (option < 1 || option > dataStructClassList.length) {
                System.out.print("> ");
                try {
                    option = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida. Por favor digite um numero valido.");
                }
            }
            return dataStructClassList[option - 1];
        }

        // prompt e cria uma lista do tipo de dado fornecido
    private static <T extends Comparable<T>> T  promptCreateType(Class<T> type) {
            System.out.print("Valor do tipo: `" + type.getSimpleName() + "`\n> ");
            return handleTypes(type);
        }

  // prompt e cria uma instancia do tipo de dado fornecido
  private static <T extends Comparable<T>> List<T> promptValueList(Class<T> type) {
      System.out.println("Tipo: " + type.getSimpleName());

      String example;
      if (type.equals(Integer.class)) {
          example = "11 15 7 46 17 2";
      } else if (type.equals(Float.class)) {
          example = "3.14 2.71828 0.63 3.69 66.6";
      } else if (type.equals(Double.class)) {
          example = "33.3 45.62 12.6 177.3 19.52 1.0";
      } else {
          example = "Formato não suportado para este tipo";
      }

      System.out.printf("Valores iniciais (ex: %s):%n> ", example);

      String input = scanner.nextLine();
      String[] values = input.split("\\s+");
      List<T> list = new ArrayList<>();
      for (String value : values) {
          list.add(handleTypes(type, value));
      }
      return list;
  }

private static <T extends Comparable<T>> T handleTypes(Class<T> type)  {
  if (type.equals(Integer.class)) {
    return type.cast(Integer.parseInt(scanner.nextLine()));
  } else if (type.equals(Float.class)) {
    return type.cast(Float.parseFloat(scanner.nextLine()));
  } else if (type.equals(Double.class)) {
    return type.cast(Double.parseDouble(scanner.nextLine()));
  } else {
    return null;
  }
}

  private static <T extends Comparable<T>> T handleTypes(Class<T> type, String value) {
      if (type.equals(Integer.class)) {
          return type.cast(Integer.parseInt(value));
      } else if (type.equals(Float.class)) {
          return type.cast(Float.parseFloat(value));
      } else if (type.equals(Double.class)) {
          return type.cast(Double.parseDouble(value));
      } else {
          throw new IllegalArgumentException("Tipo nao suportado: " + type.getSimpleName());
      }
  }
}