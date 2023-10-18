package top.zhangsiyao.betterfishing;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class PlaceholderReceiver extends PlaceholderExpansion {

    private final BetterFishing plugin;

    public PlaceholderReceiver(BetterFishing plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }


    @Override
    public @NotNull String getIdentifier() {
        return "bf";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }


    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if(identifier.equals("next_day_unix")){
            Date date=new Date();
            date.setSeconds(0);
            date.setHours(0);
            date.setMinutes(0);
            long unix=date.getTime();
            return String.valueOf((unix+86400000)/1000);
        }
        return "";
    }

//    @Override
//    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
//        if (player == null) {
//            return "";
//        }
//
//        if(identifier.equalsIgnoreCase("competition_type")) {
//            if (!Competition.isActive()) {
//                return new Message(ConfigMessage.PLACEHOLDER_NO_COMPETITION_RUNNING).getRawMessage(true, false);
//            }
//
//            return BetterFishing.active.getCompetitionType().name();
//        }
//        // %emf_competition_place_player_1% would return the player in first place of any possible competition.
//        if (identifier.startsWith("competition_place_player_")) {
//            if (!Competition.isActive()) {
//                return new Message(ConfigMessage.PLACEHOLDER_NO_COMPETITION_RUNNING).getRawMessage(true, false);
//            }
//
//            // checking the leaderboard actually contains the value of place
//            int place = Integer.parseInt(identifier.substring(25));
//            if (!leaderboardContainsPlace(place)) {
//                return new Message(ConfigMessage.PLACEHOLDER_NO_PLAYER_IN_PLACE).getRawMessage(true, false);
//            }
//
//            // getting "place" place in the competition
//            UUID uuid = BetterFishing.active.getLeaderboard().getPlayer(place);
//            if (uuid != null) {
//                // To be in the leaderboard the player must have joined
//                return Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid)).getName();
//            }
//        }
//        if (identifier.startsWith("competition_place_size_")) {
//            if (!Competition.isActive()) {
//                return new Message(ConfigMessage.PLACEHOLDER_NO_COMPETITION_RUNNING_SIZE).getRawMessage(true, false);
//            }
//            if (!(BetterFishing.active.getCompetitionType() == CompetitionType.LARGEST_FISH ||
//                BetterFishing.active.getCompetitionType() == CompetitionType.LARGEST_TOTAL)) {
//                return new Message(ConfigMessage.PLACEHOLDER_SIZE_DURING_MOST_FISH).getRawMessage(true, false);
//            }
//
//            // checking the leaderboard actually contains the value of place
//            int place = Integer.parseInt(identifier.substring(23));
//            if (!leaderboardContainsPlace(place)) {
//                return new Message(ConfigMessage.PLACEHOLDER_NO_SIZE_IN_PLACE).getRawMessage(true, false);
//            }
//
//            // getting "place" place in the competition
//            float value = BetterFishing.active.getLeaderboard().getPlaceValue(place);
//
//            if (value != -1.0f) return Float.toString(Math.round(value * 10f) / 10f);
//            else return "";
//
//        }
//        if (identifier.startsWith("competition_place_fish_")) {
//            if (!Competition.isActive()) {
//                return new Message(ConfigMessage.PLACEHOLDER_NO_COMPETITION_RUNNING_FISH).getRawMessage(true, false);
//            }
//
//            if (BetterFishing.active.getCompetitionType() == CompetitionType.LARGEST_FISH) {
//                // checking the leaderboard actually contains the value of place
//                int place = Integer.parseInt(identifier.substring(23));
//                if (!leaderboardContainsPlace(place)) {
//                    return new Message(ConfigMessage.PLACEHOLDER_NO_FISH_IN_PLACE).getRawMessage(true, false);
//                }
//
//                // getting "place" place in the competition
//                Fish fish = BetterFishing.active.getLeaderboard().getPlaceFish(place);
//                if (fish != null) {
//                    Message message = new Message(ConfigMessage.PLACEHOLDER_FISH_FORMAT);
//                    if (fish.getLength() == -1)
//                        message.setMessage(ConfigMessage.PLACEHOLDER_FISH_LENGTHLESS_FORMAT);
//                    else message.setLength(Float.toString(fish.getLength()));
//
//                    message.setRarityColour(fish.getRarity().getColour());
//
//                    if (fish.getDisplayName() != null) message.setFishCaught(fish.getDisplayName());
//                    else message.setFishCaught(fish.getName());
//
//                    if (fish.getRarity().getDisplayName() != null)
//                        message.setRarity(fish.getRarity().getDisplayName());
//                    else message.setRarity(fish.getRarity().getValue());
//
//                    return message.getRawMessage(true, true);
//                }
//
//            } else {
//                // checking the leaderboard actually contains the value of place
//                float value = Competition.leaderboard.getPlaceValue(Integer.parseInt(identifier.substring(23)));
//                if (value == -1)
//                    return new Message(ConfigMessage.PLACEHOLDER_NO_FISH_IN_PLACE).getRawMessage(true, false);
//
//                Message message = new Message(ConfigMessage.PLACEHOLDER_FISH_MOST_FORMAT);
//                message.setAmount(Integer.toString((int) value));
//                return message.getRawMessage(true, true);
//            }
//
//        }
//
//        if(identifier.startsWith("total_money_earned_")) {
//            try {
//                final UUID uuid = UUID.fromString(identifier.split("total_money_earned_")[1]);
//                final UserReport userReport = DataManager.getInstance().getUserReportIfExists(uuid);
//                if(userReport == null)
//                    return null;
//
//                return String.format("%.2f",userReport.getMoneyEarned());
//            } catch (IllegalArgumentException e) {
//                return null;
//            }
//        }
//
//        if(identifier.startsWith("total_fish_sold_")) {
//            try {
//                final UUID uuid = UUID.fromString(identifier.split("total_fish_sold_")[1]);
//                final UserReport userReport = DataManager.getInstance().getUserReportIfExists(uuid);
//                if(userReport == null)
//                    return null;
//
//                return String.valueOf(userReport.getFishSold());
//            } catch (IllegalArgumentException e) {
//                return null;
//            }
//        }
//
//        if (identifier.equals("competition_time_left")) {
//            if (Competition.isActive()) {
//                return new Message(ConfigMessage.PLACEHOLDER_TIME_REMAINING_DURING_COMP).getRawMessage(true, false);
//            }
//
//            int competitionStartTime = BetterFishing.competitionQueue.getNextCompetition();
//            int currentTime = AutoRunner.getCurrentTimeCode();
//            int remainingTime = getRemainingTime(competitionStartTime,currentTime);
//
//            Message message = new Message(ConfigMessage.PLACEHOLDER_TIME_REMAINING);
//            message.setDays(Integer.toString(remainingTime / 1440));
//            message.setHours(Integer.toString((remainingTime % 1440) / 60));
//            message.setMinutes(Integer.toString((((remainingTime % 1440) % 60) % 60)));
//
//            return message.getRawMessage(true, true);
//        }
//
//        if (identifier.equals("competition_active")) {
//            return Boolean.toString(Competition.isActive());
//        }
//
//        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
//        // was provided
//        return null;
//    }
//
//    private int getRemainingTime(int competitionStartTime, int currentTime) {
//        if (competitionStartTime > currentTime) {
//            return competitionStartTime - currentTime;
//        }
//
//        return getRemainingTimeOverWeek(competitionStartTime,currentTime);
//    }
//
//    // time left of the current week + the time next week until next competition
//    private int getRemainingTimeOverWeek(int competitionStartTime, int currentTime) {
//        return (10080 - currentTime) + competitionStartTime;
//    }
//
//    private boolean leaderboardContainsPlace(int place) {
//        return BetterFishing.active.getLeaderboardSize() >= place;
//    }
}