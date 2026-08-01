package com.geek.tao.bt10.tool;

import com.geek.common.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * POWER6 计分：迁自 docs/六大能力测评/power6/better10-power6.html
 */
@Component
public class Power6ToolHandler implements KnowledgeToolHandler {

    public static final String CODE = "POWER6";

    private static final Map<String, String> LABEL = Map.of(
            "M", "目标力", "Z", "专注力", "X", "自信力",
            "S", "适应力", "K", "抗挫力", "Q", "情绪力");

    private static final String[] DIMENSIONS = {"M", "Z", "X", "S", "K", "Q"};

    private static final List<Card> GRID = List.of(
            c(1, '+', "M", "自律性强"), c(2, '+', "Q", "情绪稳定"),
            c(3, '+', "Z", "才思敏捷"), c(4, '+', "K", "迎难而上"),
            c(5, '+', "S", "顺其自然"), c(6, '+', "K", "内心强大"),
            c(7, '-', "M", "缺乏目标"), c(8, '-', "X", "缺乏自信"),
            c(9, '+', "Z", "注意细节"), c(10, '+', "Q", "沉着冷静"),
            c(11, '-', "S", "固执刻板"), c(12, '-', "X", "心浮气躁"),
            c(13, '-', "M", "缺乏自律"), c(14, '-', "Q", "易被激怒"),
            c(15, '-', "Z", "容易分心"), c(16, '-', "K", "畏难逃避"),
            c(17, '-', "S", "争强好胜"), c(18, '-', "K", "容易受伤"),
            c(19, '+', "M", "目标力强"), c(20, '+', "X", "自信心强"),
            c(21, '-', "Z", "粗心大意"), c(22, '-', "Q", "焦虑紧张"),
            c(23, '+', "S", "灵活变通"), c(24, '+', "X", "不骄不躁")
    );

    private static Card c(int num, char sign, String letter, String desc) {
        return new Card(num, sign, letter, desc);
    }

    private record Card(int num, char sign, String letter, String desc) {}

    @Override
    public String toolCode() {
        return CODE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(Map<String, Object> answers) {
        List<Integer> very = toIntList(answers.get("very"));
        List<Integer> compare = toIntList(answers.get("compare"));
        List<Integer> occasional = toIntList(answers.get("occasional"));
        if (very.size() != 4 || compare.size() != 4 || occasional.size() != 4) {
            throw new ServiceException("每个框必须填入 4 个有效数字(1-24)");
        }
        List<Integer> all = new ArrayList<>();
        all.addAll(very);
        all.addAll(compare);
        all.addAll(occasional);
        Set<Integer> seen = new HashSet<>();
        for (Integer n : all) {
            if (n == null || n < 1 || n > 24) {
                throw new ServiceException("编号必须在 1-24");
            }
            if (!seen.add(n)) {
                throw new ServiceException("编号不可重复: " + n);
            }
        }
    }

    @Override
    public Map<String, Object> score(Map<String, Object> answers) {
        validate(answers);
        List<Integer> very = toIntList(answers.get("very"));
        List<Integer> compare = toIntList(answers.get("compare"));
        List<Integer> occasional = toIntList(answers.get("occasional"));

        Map<String, Integer> totals = new LinkedHashMap<>();
        Map<String, List<Map<String, Object>>> details = new LinkedHashMap<>();
        for (String d : DIMENSIONS) {
            totals.put(d, 0);
            details.put(d, new ArrayList<>());
        }
        apply(very, 5, totals, details);
        apply(compare, 3, totals, details);
        apply(occasional, 1, totals, details);

        List<Map<String, Object>> cards = new ArrayList<>();
        for (String key : DIMENSIONS) {
            int score = totals.get(key);
            Map<String, Object> card = new LinkedHashMap<>();
            card.put("key", key);
            card.put("label", LABEL.get(key));
            card.put("score", score);
            card.put("advice", advice(key, score));
            card.put("details", details.get(key));
            cards.add(card);
        }

        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object> scoreSummary = new LinkedHashMap<>();
        scoreSummary.put("totals", totals);
        scoreSummary.put("labels", LABEL);
        out.put("scoreSummary", scoreSummary);
        Map<String, Object> basic = new LinkedHashMap<>();
        basic.put("cards", cards);
        basic.put("inputs", Map.of(
                "very", very,
                "compare", compare,
                "occasional", occasional));
        out.put("resultBasic", basic);
        return out;
    }

    @Override
    public Map<String, Object> buildAiContext(Map<String, Object> answers, Map<String, Object> scoreResult) {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("answers", answers);
        ctx.put("scoreSummary", scoreResult.get("scoreSummary"));
        ctx.put("resultBasic", scoreResult.get("resultBasic"));
        return ctx;
    }

    private void apply(List<Integer> nums, int weight,
                       Map<String, Integer> totals,
                       Map<String, List<Map<String, Object>>> details) {
        for (Integer n : nums) {
            Card item = GRID.stream().filter(g -> g.num == n).findFirst().orElse(null);
            if (item == null) continue;
            int val = (item.sign == '+' ? 1 : -1) * weight;
            totals.put(item.letter, totals.get(item.letter) + val);
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("num", item.num);
            d.put("desc", item.desc);
            d.put("sign", String.valueOf(item.sign));
            d.put("val", val);
            details.get(item.letter).add(d);
        }
    }

    private static String advice(String key, int score) {
        return switch (key) {
            case "M" -> score < 0 ? "想法多缺路径，需找导师或定OKR。" : "目标感强，继续保持并带领他人。";
            case "Z" -> score < 0 ? "易分心，建议断舍离，死磕一件事。" : "专注在线，注意细节转化产出。";
            case "X" -> score < 0 ? "缺结果自信低，先达成小目标。" : "自信满满，利用气场攻克难关。";
            case "S" -> score < 0 ? "适应难，暂守舒适区，稳住基本盘。" : "适应强，如水灵活，适合探索新机。";
            case "K" -> score > 0 ? "韧性强，正经历挑战，注意身心平衡。" : "易受伤，寻找支持系统，别独自硬扛。";
            case "Q" -> score < 0 ? "情绪波动大，需转化为动力。" : "情绪平稳，理性决策是核心优势。";
            default -> "";
        };
    }

    @SuppressWarnings("unchecked")
    private static List<Integer> toIntList(Object raw) {
        List<Integer> out = new ArrayList<>();
        if (raw == null) return out;
        if (raw instanceof List<?> list) {
            for (Object o : list) {
                if (o == null) continue;
                if (o instanceof Number n) out.add(n.intValue());
                else {
                    String s = o.toString().trim();
                    if (!s.isEmpty()) out.add(Integer.parseInt(s));
                }
            }
            return out;
        }
        String s = raw.toString().trim();
        if (s.isEmpty()) return out;
        for (String part : s.split("[,.\\s/|、。]+")) {
            if (part.isBlank()) continue;
            out.add(Integer.parseInt(part.trim()));
        }
        return out;
    }
}
