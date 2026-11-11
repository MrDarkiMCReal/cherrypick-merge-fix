package org.mrdarkimc.cherrypickfix;

import git4idea.commands.*;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class CherryPickEventHandler extends GitImpl {
    private static final Pattern MERGE_INTO_MASTER = Pattern.compile("^Merge branch '.+' into 'master'.*$");

    @Override
    public @NotNull GitCommandResult cherryPick(@NotNull GitRepository repository,
                                                @NotNull String hash,
                                                boolean autoCommit,
                                                boolean addCherryPickedFromSuffix,
                                                GitLineHandlerListener @NotNull ... listeners) {

        if (!isMergeIntoMaster(repository, hash)) {
            return super.cherryPick(repository, hash, autoCommit, addCherryPickedFromSuffix, listeners);
        }

        GitLineHandler handler = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.CHERRY_PICK);
        handler.setStdoutSuppressed(false);
        handler.addParameters("-m", "1", "-x");

        if (!autoCommit) {
            handler.addParameters("-n");
        }
        handler.addParameters(hash);
        for (GitLineHandlerListener listener : listeners) {
            handler.addLineListener(listener);
        }
        return runCommand(handler);
    }

    private boolean isMergeIntoMaster(@NotNull GitRepository repository, @NotNull String hash) {
        GitLineHandler handler = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.LOG);
        handler.setSilent(true);
        handler.addParameters("-1", "--format=%s", hash);
        GitCommandResult result = runCommand(handler);
        if (!result.success()) {
            return false;
        }
        String subject = result.getOutputAsJoinedString().trim();
        return MERGE_INTO_MASTER.matcher(subject).matches();
    }
}
