/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.git.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;

import java.io.IOException;
import java.util.List;

/**
 * Common operations related to git
 *
 * @author joseross
 * @since 4.0
 */
public abstract class GitUtils {

    public static List<DiffEntry> doDiff(Git git, ObjectReader reader, ObjectId fromCommitId,
                                     ObjectId toCommitId) throws IOException, GitAPIException {
        AbstractTreeIterator fromTreeIter = getTreeIteratorForCommit(git, reader, fromCommitId);
        AbstractTreeIterator toTreeIter = getTreeIteratorForCommit(git, reader, toCommitId);

        return git.diff().setOldTree(fromTreeIter).setNewTree(toTreeIter).call();
    }

    public static AbstractTreeIterator getTreeIteratorForCommit(Git git, ObjectReader reader,
                                                            ObjectId commitId) throws IOException {
        if (commitId != null) {
            RevTree tree = getTreeForCommit(git.getRepository(), commitId);
            CanonicalTreeParser treeParser = new CanonicalTreeParser();

            treeParser.reset(reader, tree.getId());

            return treeParser;
        } else {
            return new EmptyTreeIterator();
        }
    }

    public static RevTree getTreeForCommit(Repository repo, ObjectId commitId) throws IOException  {
        try (RevWalk revWalk = new RevWalk(repo)) {
            RevCommit commit = revWalk.parseCommit(commitId);

            return commit.getTree();
        }
    }

}
