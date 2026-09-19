private final Map<Integer, List<String>> Priority = Map.of(
        1, List.of("+", "-"),
        2, List.of("/", "*")
);

private final List<String> parentesis = List.of(
        "(",
        ")"
);

public Boolean isParentesis (String s) {
    return parentesis.contains(s);
}

public Boolean isNumber(String str) {
    if (str == null) {
        return false;
    }
    try {
        Double.parseDouble(str);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}

public boolean isOperator(String str) {
    return getPriority(str) != -1;
}

public int getPriority(String str) {
    return Priority.entrySet().stream()
            .filter(
                    e -> e.getValue().contains(str))
            .findFirst()
            .map(Map.Entry::getKey)
            .orElse(-1);
}

public Deque<String> rpn(String[] str) {
    Deque<String> output = new ArrayDeque<>();
    Deque<String> queue = new ArrayDeque<>();

    for (int i = 0; i < str.length; i++) {
        StringBuilder value = new StringBuilder();

        if (isNumber(String.valueOf(str[i]))) {
            while (i <= str.length - 1 && !isOperator(String.valueOf(str[i]))
            && !isParentesis(str[i])) {
                value.append(str[i]);
                i++;
            }
            i--;
            output.offer(String.valueOf(value));
            continue;
        }

        if (str[i].equals("(")) {
            queue.push(String.valueOf(str[i]));
            continue;
        }

        if (str[i].equals(")")) {
            while (queue.peek() != null && !queue.peek().equals("(")) {
                output.offer(queue.pop());
            }

            if (queue.peek() == null) {
                throw new ArithmeticException("No hay parentesis de cierre");
            }

            queue.pop();
            continue;
        }

        if (isOperator(String.valueOf(str[i]))) {
            while (!queue.isEmpty() && getPriority(queue.peek()) >= getPriority(str[i])) {
                output.offer(queue.pop());
            }
            queue.push(String.valueOf(str[i]));
        }
    }

    while (!queue.isEmpty() && !queue.peek().equals("(")) {
        output.offer(queue.pop());
    }

    if(!queue.isEmpty()) {
        if (queue.peek().equals("(")) throw new OperatorMissException("Te falto parentesis de cierre");
    }

    return output;
}

public Double resolveRPN(Deque<String> output) {
    Deque<Double> resultRPN = new ArrayDeque<>();
    while (!output.isEmpty()) {
        String str = output.pop();

        if (isNumber(str)) {
            resultRPN.addLast(Double.parseDouble(str));
        } else {
            double b = resultRPN.pollLast();
            double a = resultRPN.pollLast();
            switch (str) {
                case "+" -> resultRPN.addLast(a + b);
                case "-" -> resultRPN.addLast(a - b);
                case "*" -> resultRPN.addLast(a * b);
                case "/" -> resultRPN.addLast(a / b);
            }
        }
        System.out.println(resultRPN);
    }
    return resultRPN.pop();
}

void main() {
    String excersice = "( 19.5 + ( 4 + 6 * 9 ) - 8 ) + 1 - 5 * 2 + 8 / 8";
    String[] arr = excersice.split("(?<=[+\\-*/])|(?=[+\\-*/])");
    System.out.println(Arrays.toString(arr));
    System.out.println(rpn(arr));
    System.out.println(resolveRPN(rpn(arr)));
}