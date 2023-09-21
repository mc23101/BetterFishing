package top.zhangsiyao.betterfishing;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.jetbrains.annotations.NotNull;

public class PlaceholderReceiver extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return null;
    }

    @Override
    public @NotNull String getAuthor() {
        return null;
    }

    @Override
    public @NotNull String getVersion() {
        return null;
    }
//
//    private final BetterFishing plugin;
//
//    /**
//     * Since we register the expansion inside our own plugin, we
//     * can simply use this method here to get an instance of our
//     * plugin.
//     *
//     * @param plugin The instance of our plugin.
//     */
//    public PlaceholderReceiver(BetterFishing plugin) {
//        this.plugin = plugin;
//    }
//
//    /**
//     * Because this is an internal class,
//     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
//     * PlaceholderAPI is reloaded
//     *
//     * @return true to persist through reloads
//     */
//    @Override
//    public boolean persist() {
//        return true;
//    }
//
//    /**
//     * Because this is a internal class, this check is not needed
//     * and we can simply return {@code true}
//     *
//     * @return Always true since it's an internal class.
//     */
//    @Override
//    public boolean canRegister() {
//        return true;
//    }
//
//    /**
//     * The name of the person who created this expansion should go here.
//     * <br>For convenience do we return the author from the plugin.yml
//     *
//     * @return The name of the author as a String.
//     */
//    @Override
//    public @NotNull String getAuthor() {
//        return plugin.getDescription().getAuthors().toString();
//    }
//
//    /**
//     * The placeholder identifier should go here.
//     * <br>This is what tells PlaceholderAPI to call our onRequest
//     * method to obtain a value if a placeholder starts with our
//     * identifier.
//     * <br>The identifier has to be lowercase and can't contain _ or %
//     *
//     * @return The identifier in {@code %<identifier>_<value>%} as String.
//     */
//    @Override
//    public @NotNull String getIdentifier() {
//        return "emf";
//    }
//
//    /**
//     * This is the version of the expansion.
//     * <br>You don't have to use numbers, since it is set as a String.
//     * <p>
//     * For convenience do we return the version from the plugin.yml
//     *
//     * @return The version as a String.
//     */
//    @Override
//    public @NotNull String getVersion() {
//        return plugin.getDescription().getVersion();
//    }
//
//    /**
//     * This is the method called when a placeholder with our identifier
//     * is found and needs a value.
//     * <br>We specify the value identifier in this method.
//     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
//     *
//     * @param player     A {@link org.bukkit.entity.Player Player}.
//     * @param identifier A String containing the identifier/value.
//     * @return possibly-null String of the requested identifier.
//     */
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